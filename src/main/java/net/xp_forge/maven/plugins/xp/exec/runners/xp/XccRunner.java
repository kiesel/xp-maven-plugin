/**
 * This file is part of the XP-Framework
 *
 * XP-Framework Maven plugin
 * Copyright (c) 2011, XP-Framework Team
 */
package net.xp_forge.maven.plugins.xp.exec.runners.xp;

import java.io.File;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

import net.xp_forge.maven.plugins.xp.exec.RunnerException;
import net.xp_forge.maven.plugins.xp.exec.input.xp.XccRunnerInput;

/**
 * Wrapper over XP-Framework "xcc" runner
 *
 */
public class XccRunner extends AbstractClasspathRunner {
  private XccRunnerInput input;

  /**
   * Constructor
   *
   * @param  java.io.File executable
   * @param  net.xp_forge.maven.plugins.xp.exec.input.xp.XccRunnerInput input
   */
  public XccRunner(File executable, XccRunnerInput input) {
    super(executable);
    this.input= input;
  }

  /**
   * {@inheritDoc}
   *
   */
  public void execute() throws RunnerException {

    // Build arguments
    List<String> arguments= new ArrayList<String>();

    // Configure classpath (via project.pth)
    File pthFile= new File(this.getWorkingDirectory(), "project.pth");
    this.setClasspath(this.input.classpaths, pthFile);

    // Add verbose (-v)
    if (this.input.verbose) arguments.add("-v");

    // Add sourcepath (-sp)
    for (File sp : this.input.sourcepaths) {
      arguments.add("-sp");
      arguments.add(sp.getAbsolutePath());
    }

    // Add emitter (-e)
    if (null != this.input.emitter && 0 != this.input.emitter.trim().length()) {
      arguments.add("-e");
      arguments.add(this.input.emitter);
    }

    // Add profile (-p)
    if (!this.input.profiles.isEmpty()) {
      StringBuffer profilesBuff= new StringBuffer();
      Iterator it= this.input.profiles.iterator();
      while (it.hasNext()) {
        profilesBuff.append((String)it.next());
        if (it.hasNext()) profilesBuff.append(",");
      }

      arguments.add("-p");
      arguments.add(profilesBuff.toString());
    }

    // Add output (-o)
    if (null == this.input.outputdir) {
      throw new RunnerException("xcc outputdir not set");
    }
    arguments.add("-o");
    arguments.add(this.input.outputdir.getAbsolutePath());

    // Add sources
    for (File src : this.input.sources) {
      arguments.add(src.getAbsolutePath());
    }

    // Execute command
    this.executeCommand(arguments);
  }
}
