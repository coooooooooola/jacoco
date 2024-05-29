package com.swhysc.cov.utils;

import com.swhysc.cov.model.enums.BizCode;
import com.swhysc.cov.model.enums.GitWorkSpaceStatusEnum;
import com.swhysc.cov.model.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.lib.RepositoryCache;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.eclipse.jgit.util.FS;
import org.eclipse.jgit.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
public class GitHandler {
    static final Logger logger = LoggerFactory.getLogger(GitHandler.class);

//    @Value(value = "${git.username}")
//    private String username;
//
//    @Value(value = "${git.password}")
//    private String password;

    public static Git cloneRepository(String gitUrl, String codePath, String commitId) throws GitAPIException, IOException {
        GitWorkSpaceStatusEnum workSpaceStatus = checkGitWorkSpace(gitUrl, codePath);
        //如果当前路径已有.git且与gitUrl不一致，抛异常退出
        if (workSpaceStatus == GitWorkSpaceStatusEnum.OTHER) {
            throw new BizException(BizCode.GIT_CLONE_OTHER_REPOSITY_EXSIT_ERROR);
        }
        Git git;
        if (workSpaceStatus == GitWorkSpaceStatusEnum.EMPTY) {
            git = Git.cloneRepository()
                    .setURI(gitUrl)
//                .setCredentialsProvider(new UsernamePasswordCredentialsProvider(username, password))
                    .setCredentialsProvider(new UsernamePasswordCredentialsProvider("230674", "swhy7875"))
                    .setDirectory(new File(codePath))
                    .setBranch(commitId)
                    .call();
            // 切换到指定commitId/branch
            checkoutBranch(git, commitId);
        } else {
            git = Git.open(new File(codePath));

            git.getRepository().getFullBranch();
            //如果是在某个commit上返回false，如果是在某个分支上返回true
            if (git.getRepository().exactRef(Constants.HEAD).isSymbolic()) {
                //更新代码 【需要用本地代码时，注释掉下一行】
//                instanceHttpGit(gitUrl, codePath, commitId, git, gitUserName, gitPassWord);
            }
        }
        return git;
    }

    private static Ref checkoutBranch(Git git, String branch) {
        try {
            return git.checkout()
                    .setName(branch)
                    .call();
        } catch (GitAPIException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * 检查工作区是否存在
     *
     * @param gitUrl:   传入的git仓库地址
     * @param codePath: 代码保存路径
     * @return
     * @throws IOException
     */
    public static GitWorkSpaceStatusEnum checkGitWorkSpace(String gitUrl, String codePath) throws IOException {
        Boolean isExist = Boolean.FALSE;
        File repoGitDir = new File(codePath + "/.git");
        if (!repoGitDir.exists()) {
            return GitWorkSpaceStatusEnum.EMPTY;
        }
        Git git = Git.open(new File(codePath));
        if (null == git) {
            return GitWorkSpaceStatusEnum.EMPTY;
        }
        Repository repository = git.getRepository();
        //解析本地代码，获取远程uri,是否是我们需要的git远程仓库
        String repoUrl = repository.getConfig().getString("remote", "origin", "url");
        if (gitUrl.equals(repoUrl)) {
            return GitWorkSpaceStatusEnum.EXIST;
        }
        log.info("本地存在其他仓的代码，先删除");
        git.close();
//        FileUtils.delete(new File(codePath));
        return GitWorkSpaceStatusEnum.OTHER;
    }

    public static void main(String[] args) throws GitAPIException, IOException {
        GitHandler.cloneRepository("https://devops.swhysc.com:9000/enterprise/enterprise__csgjyfkj/csui-server.git", "D:/jacoco", "70b3ee13b7f23f2c94e7979ed26809024d575ea6");
    }

}
