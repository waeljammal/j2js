package com.rsnm;

import con.rsnm.J2JS;
import org.apache.maven.plugin.testing.MojoRule;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.junit.Rule;
import org.junit.Test;

import java.io.File;

public class J2JSTest extends AbstractMojoTestCase
{
    @Rule
    public MojoRule rule = new MojoRule()
    {
        @Override
        protected void before() throws Throwable
        {
        }

        @Override
        protected void after()
        {
        }
    };

    /**
     * @throws Exception if any
     */
    @Test
    public void testSomething() throws Exception
    {

        File pom = getTestFile( "src/test/resources/pom.xml" );
        assertNotNull( pom );
        assertTrue( pom.exists() );

        J2JS myMojo = (J2JS) lookupMojo( "j2js", pom );
        assertNotNull( myMojo );
        myMojo.execute();
    }
}