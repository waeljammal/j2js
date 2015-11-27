package com.rsnm;

import com.rsnm.lib.entity.Account;
import com.rsnm.lib.entity.Portal;
import com.rsnm.lib.entity.PortalStatus;
import com.rsnm.lib.entity.User;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.repository.DefaultArtifactRepository;
import org.apache.maven.artifact.repository.layout.DefaultRepositoryLayout;
import org.apache.maven.plugin.testing.stubs.MavenProjectStub;

import java.util.Collections;
import java.util.List;

public class J2JSStub extends MavenProjectStub
{
    private Account account;
    private Portal portal;
    private PortalStatus status;
    private User user;

    /**
     * Default constructor
     */
    public J2JSStub()
    {

    }

    /** {@inheritDoc} */
    public List getRemoteArtifactRepositories()
    {
        ArtifactRepository repository = new DefaultArtifactRepository( "central", "http://repo.maven.apache.org/maven2",
                                                                       new DefaultRepositoryLayout() );

        return Collections.singletonList(repository);
    }
}