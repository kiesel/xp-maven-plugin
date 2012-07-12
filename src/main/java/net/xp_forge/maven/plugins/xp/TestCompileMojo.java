/**
 * This file is part of the XP-Framework
 *
 * Maven plugin for XP-Framework
 * Copyright (c) 2011, XP-Framework Team
 */
package net.xp_forge.maven.plugins.xp;

import java.io.File;
import java.util.List;
import java.util.ArrayList;

import org.apache.maven.plugin.MojoExecutionException;
import net.xp_forge.maven.plugins.xp.util.FileUtils;

/**
 * Run XP Framework XCC compiler (compile test .xp sources)
 *
 * @goal test-compile
 * @requiresDependencyResolution compile
 */
public class TestCompileMojo extends AbstractCompileMojo {

  /**
   * Set this to 'true' to bypass unit tests entirely
   * Its use is NOT RECOMMENDED, but quite convenient on occasion
   *
   * @parameter expression="${maven.test.skip}" default-value="false"
   */
  private boolean skip;

  /**
   * The source directories containing the raw PHP sources to be copied
   * Default value: [src/test/php]
   *
   * @parameter
   */
  private List<String> testPhpSourceRoots;

  /**
   * The source directories containing the sources to be compiled
   * Default value: [src/test/xp]
   *
   * @parameter expression="${project.testCompileSourceRoots}"
   * @required
   * @readonly
   */
  private List<String> testCompileSourceRoots;

  /**
   * {@inheritDoc}
   *
   */
  @Override
  protected List<String> getPhpSourceRoots() {
    if (null == this.testPhpSourceRoots || this.testPhpSourceRoots.isEmpty()) {
      this.testPhpSourceRoots= new ArrayList<String>();
      this.testPhpSourceRoots.add("src" + File.separator + "test" + File.separator + "php");
    }
    return this.testPhpSourceRoots;
  }

  /**
   * {@inheritDoc}
   *
   */
  @Override
  protected List<String> getCompileSourceRoots() {
    return this.testCompileSourceRoots;
  }

  /**
   * {@inheritDoc}
   *
   */
  @Override
  protected String getAdditionalClasspath() {
    return this.classesDirectory.getAbsolutePath();
  }

  /**
   * {@inheritDoc}
   *
   */
  protected File getClassesDirectory() {
    return this.testClassesDirectory;
  }

  /**
   * {@inheritDoc}
   *
   */
  @Override
  protected boolean isSkip() {
    return this.skip;
  }
}
