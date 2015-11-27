package con.rsnm;

import con.rsnm.adapters.IAdapter;
import con.rsnm.model.JSClass;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactNotFoundException;
import org.apache.maven.artifact.resolver.ArtifactResolutionException;
import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.eclipse.aether.impl.ArtifactResolver;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import javax.persistence.Entity;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Mojo( name = "j2js", defaultPhase = LifecyclePhase.COMPILE )
public class J2JS extends AbstractMojo
{
    /**
     * Location of the file.
     */
    @Parameter( defaultValue = "${project.build.directory}", property = "outputDir", required = true )
    private File outputDirectory;

    @Parameter( property = "entityPackage", defaultValue = "com.rsnm.lib.entity" )
    private String entityPackage;

    @Parameter( property = "entityPackage", defaultValue = "com.rsnm.lib.rest" )
    private String restPackage;

    @Parameter( property = "entityAdapter", defaultValue = "com.rsnm.adapters.hibernate.HibernateEntityAdapter" )
    private String entityAdapter;

    @Component
    private MavenProject project;

    public void execute() throws MojoExecutionException
    {
        List<String> classpathElements = null;
        try {
            classpathElements = project.getCompileClasspathElements();
            List<URL> projectClasspathList = new ArrayList<URL>();
            for (String element : classpathElements) {
                try {
                    projectClasspathList.add(new File(element).toURI().toURL());
                } catch (MalformedURLException e) {
                    throw new MojoExecutionException(element + " is an invalid classpath element", e);
                }
            };

            URLClassLoader loader = new URLClassLoader(projectClasspathList.toArray(new URL[0]));

            Reflections reflections = new Reflections(
                    new ConfigurationBuilder().setUrls(
                            ClasspathHelper.forPackage(this.entityPackage, loader)
                    ).addClassLoader(loader)
            );

            IAdapter entityParsingAdapter = null;

            try {
                entityParsingAdapter = (IAdapter) Class.forName(entityAdapter).newInstance();
            } catch (InstantiationException e) {
                new MojoExecutionException("Missing entity adapter", e);
            } catch (IllegalAccessException e) {
                new MojoExecutionException("Missing entity adapter", e);
            }

            entityParsingAdapter.parse(reflections);

        } catch (DependencyResolutionRequiredException e) {
            new MojoExecutionException("Dependency resolution failed", e);
        } catch (ClassNotFoundException e) {
            new MojoExecutionException("Missing entity adapter", e);
        }
    }
}
