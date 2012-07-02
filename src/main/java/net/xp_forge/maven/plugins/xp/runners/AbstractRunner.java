/**
 * This file is part of the XP-Framework
 *
 * Maven plugin for XP-Framework
 * Copyright (c) 2011, XP-Framework Team
 */
package net.xp_forge.maven.plugins.xp.runners;

import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import org.apache.commons.lang.StringUtils;
import org.apache.maven.plugin.logging.Log;

import net.xp_forge.maven.plugins.xp.util.FileUtils;
import net.xp_forge.maven.plugins.xp.util.ExecuteUtils;
import net.xp_forge.maven.plugins.xp.runners.RunnerException;

/**
 * Base class for all XP-Framework runners
 *
 */
public abstract class AbstractRunner {
  private Log cat;
  private File executable;
  private File workingDirectory;
  private Map<String, String> environmentVariables= new HashMap<String, String>();

  /**
   * Constructor
   *
   * @param  java.io.File executable
   */
  public AbstractRunner(File executable) {
    this.executable= executable;
  }

  /**
   * Execute this runner
   *
   * @throws net.xp_forge.maven.plugins.xp.runners.RunnerException When runner execution failed
   */
  public abstract void execute() throws RunnerException;

  /**
   * Set logging trace
   *
   * @param  org.apache.maven.plugin.logging.Log cat
   * @return void
   */
  public void setTrace(Log cat) {
    this.cat= cat;
  }

  /**
   * Output an INFO message to log
   *
   * @param  java.lang.String msg Message to output
   * @return void
   */
  protected void info(String msg) {
    if (null == this.cat) return;
    this.cat.info(msg);
  }

  /**
   * Output an ERROR message to log
   *
   * @param  java.lang.String msg Message to output
   * @return void
   */
  protected void error(String msg) {
    if (null == this.cat) return;
    this.cat.error(msg);
  }

  /**
   * Output an ERROR message to log
   *
   * @param  java.lang.String msg Message to output
   * @param  java.lang.Throwable ex Caught exception
   * @return void
   */
  protected void error(String msg, Throwable ex) {
    if (null == this.cat) return;
    this.cat.error(msg, ex);
  }

  /**
   * Get executable
   *
   * @return java.io.File
   */
  public File getExecutable() {
    return this.executable;
  }

  /**
   * Set runner working directory
   *
   * @param  java.io.File workingDirectory
   * @return void
   * @throws java.io.FileNotFoundException When working directory does not exist
   */
  public void setWorkingDirectory(File workingDirectory) throws FileNotFoundException {

    // Check directory exists
    if (!workingDirectory.exists()) {
      throw new FileNotFoundException("Working directory not found [" + workingDirectory + "]");
    }

    // Set working directory
    this.workingDirectory= workingDirectory;
  }

  /**
   * Get working directory; default to current directory
   *
   * @return java.io.File
   */
  public File getWorkingDirectory() {
    if (null == this.workingDirectory || !this.workingDirectory.exists()) {
      this.workingDirectory= new File(System.getProperty("user.dir"));
    }
    return this.workingDirectory;
  }

  /**
   * Set runner environment variable
   *
   * @param  java.lang.String name
   * @param  java.lang.String value
   * @return void
   */
  public void setEnvironmentVariable(String name, String value) {
    this.environmentVariables.put(name, value);
  }

  /**
   * Get runner environment variable
   *
   * @param  java.lang.String name
   * @return java.lang.String
   */
  public String getEnvironmentVariable(String name) {
    return this.environmentVariables.get(name);
  }

  /**
   * Get all runner environment variables
   *
   * @return java.util.Map<java.lang.String>
   */
  public Map<String, String> getEnvironmentVariables() {
    return this.environmentVariables;
  }

  /**
   * Execute command using the specified arguments
   *
   * @param  java.util.List<String> arguments Executable arguments
   * @return void
   * @throws net.xp_forge.maven.plugins.xp.runners.RunnerException When execution failed
   */
  protected void executeCommand(List<String> arguments) throws RunnerException {
    try {
      ExecuteUtils.executeCommand(
        this.getExecutable(),
        arguments,
        this.getWorkingDirectory(),
        this.getEnvironmentVariables(),
        this.cat
      );
    } catch (ExecutionException ex) {
      throw new RunnerException("Execution failed", ex);
    }
  }

  /**
   * Set classpath via command line arguments
   *
   * @param  java.util.List<java.lang.String> classpaths
   * @param  java.util.List<java.lang.String> arguments
   * @return void
   */
  public void setClasspath(List<String> classpaths, List<String> arguments) {
    for (String classpath : classpaths) {
      arguments.add("-cp");
      arguments.add(classpath);
    }
  }

  /**
   * Set classpath via [project.pth] file
   *
   * @param  java.util.List<java.lang.String> classpaths
   * @param  java.io.File pthFile
   * @return void
   * @throws net.xp_forge.maven.plugins.xp.runners.RunnerException When cannot create project.pth file
   */
  public void setClasspath(List<String> classpaths, File pthFile) throws RunnerException {
    try {
      FileUtils.setFileContents(pthFile, StringUtils.join(classpaths, "\n"));
    } catch (IOException ex) {
      throw new RunnerException("Cannot write [" + pthFile + "] file", ex);
    }
  }
}