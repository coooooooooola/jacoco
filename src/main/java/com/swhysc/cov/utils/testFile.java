package com.swhysc.cov.utils;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;

public class testFile {
    public static Git instanceHttpGit(String gitUrl, String codePath, String commitId, Git git, String gitUserName, String gitPassWord) throws GitAPIException {
        if (null != git) {
            git.pull().setCredentialsProvider(new UsernamePasswordCredentialsProvider(gitUserName, gitPassWord)).call();
            return git;
        }
        return Git.cloneRepository()
                .setURI(gitUrl)
                .setCredentialsProvider(new UsernamePasswordCredentialsProvider(gitUserName, gitPassWord))
                .setDirectory(new File(codePath))
                .setBranch(commitId)
                .call();
    }
}
