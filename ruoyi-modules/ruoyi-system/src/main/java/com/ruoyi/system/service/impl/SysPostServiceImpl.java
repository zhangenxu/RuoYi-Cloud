package com.ruoyi.system.service.impl;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;
import com.ruoyi.common.core.constant.UserConstants;
import com.ruoyi.common.core.exception.ServiceException;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.system.domain.SysPost;
import com.ruoyi.system.mapper.SysPostMapper;
import com.ruoyi.system.mapper.SysUserPostMapper;
import com.ruoyi.system.service.ISysPostService;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;
import javax.annotation.Resource;

/**
 * 岗位信息 服务层处理
 * 
 * @author ruoyi
 */
@Service
public class SysPostServiceImpl implements ISysPostService
{
    @Resource
    private SysPostMapper postMapper;

    @Resource
    private SysUserPostMapper userPostMapper;

    /**
     * 查询岗位信息集合
     * 
     * @param post 岗位信息
     * @return 岗位信息集合
     */
    @Override
    public List<SysPost> selectPostList(SysPost post){
        return postMapper.selectPostList(post);
    }

    /**
     * 查询所有岗位
     * 
     * @return 岗位列表
     */
    @Override
    public List<SysPost> selectPostAll(){
        return postMapper.selectAll();//postMapper.selectPostAll();
    }

    /**
     * 通过岗位ID查询岗位信息
     * 
     * @param postId 岗位ID
     * @return 角色对象信息
     */
    @Override
    public SysPost selectPostById(Long postId){
        return postMapper.selectByPrimaryKey(postId);// postMapper.selectPostById(postId);
    }

    /**
     * 根据用户ID获取岗位选择框列表
     * 
     * @param userId 用户ID
     * @return 选中岗位ID列表
     */
    @Override
    public List<Long> selectPostListByUserId(Long userId){
        Example e = new Example(SysPost.class);
        Example.Criteria criteria = e.createCriteria();
        criteria.andEqualTo("usrId",userId);
        List<SysPost> posts = postMapper.selectByExample(e);
        List<Long> ids = posts.stream().map(SysPost::getPostId).collect(Collectors.toList());
        return ids;// postMapper.selectPostListByUserId(userId);
    }

    /**
     * 校验岗位名称是否唯一
     * 
     * @param post 岗位信息
     * @return 结果
     */
    @Override
    public boolean checkPostNameUnique(SysPost post){
        Long postId = StringUtils.isNull(post.getPostId()) ? -1L : post.getPostId();
        //通过名称查询岗位
        Example e = new Example(SysPost.class);
        Example.Criteria criteria = e.createCriteria();
        criteria.andEqualTo("postName",post.getPostName());
        RowBounds rb = new RowBounds(0,1);
        List<SysPost> posts = postMapper.selectByExampleAndRowBounds(e,rb);
        SysPost info = CollectionUtils.isEmpty(posts)?null:posts.get(0);
        //postMapper.checkPostNameUnique(post.getPostName());
        if (StringUtils.isNotNull(info) && info.getPostId().longValue() != postId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 校验岗位编码是否唯一
     * 
     * @param post 岗位信息
     * @return 结果
     */
    @Override
    public boolean checkPostCodeUnique(SysPost post){
        Long postId = StringUtils.isNull(post.getPostId()) ? -1L : post.getPostId();
        //通过代码查询岗位
        Example e = new Example(SysPost.class);
        Example.Criteria criteria = e.createCriteria();
        criteria.andEqualTo("postCode",post.getPostCode());
        RowBounds rb = new RowBounds(0,1);
        List<SysPost> posts = postMapper.selectByExampleAndRowBounds(e,rb);
        SysPost info = CollectionUtils.isEmpty(posts)?null:posts.get(0);
        //SysPost info = postMapper.checkPostCodeUnique(post.getPostCode());
        if (StringUtils.isNotNull(info) && info.getPostId().longValue() != postId.longValue()){
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 通过岗位ID查询岗位使用数量
     * @param postId 岗位ID
     * @return 结果
     */
    @Override
    public int countUserPostById(Long postId){
        return userPostMapper.countUserPostById(postId);
    }

    /**
     * 删除岗位信息
     * 
     * @param postId 岗位ID
     * @return 结果
     */
    @Override
    public int deletePostById(Long postId){
        return postMapper.deleteByPrimaryKey(postId);
        //return postMapper.deletePostById(postId);
    }

    /**
     * 批量删除岗位信息
     * 
     * @param postIds 需要删除的岗位ID
     * @return 结果
     */
    @Override
    public int deletePostByIds(Long[] postIds){
        for (Long postId : postIds){
            SysPost post = selectPostById(postId);
            if (countUserPostById(postId) > 0){
                throw new ServiceException(String.format("%1$s已分配,不能删除", post.getPostName()));
            }
        }
        Example e = new Example(SysPost.class);
        Example.Criteria criteria = e.createCriteria();
        criteria.andIn("postId", Arrays.asList(postIds));
        return postMapper.deleteByExample(e);// postMapper.deletePostByIds(postIds);
    }

    /**
     * 新增保存岗位信息
     * 
     * @param post 岗位信息
     * @return 结果
     */
    @Override
    public int insertPost(SysPost post){
        return postMapper.insertSelective(post);// postMapper.insertPost(post);
    }

    /**
     * 修改保存岗位信息
     * 
     * @param post 岗位信息
     * @return 结果
     */
    @Override
    public int updatePost(SysPost post) {
        return postMapper.updateByPrimaryKey(post);// postMapper.updatePost(post);
    }
}
