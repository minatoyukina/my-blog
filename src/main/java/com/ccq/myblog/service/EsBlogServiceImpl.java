package com.ccq.myblog.service;

import com.ccq.myblog.domain.EsBlog;
import com.ccq.myblog.domain.User;
import com.ccq.myblog.repository.es.EsBlogRepository;
import com.ccq.myblog.vo.TagVO;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
import static org.elasticsearch.search.aggregations.AggregationBuilders.terms;

@Service
public class EsBlogServiceImpl implements EsBlogService {

    @Autowired
    private EsBlogRepository esBlogRepository;
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;
    @Autowired
    private UserService userService;

    private static final Pageable TOP_5_PAGEABLE = PageRequest.of(0, 5);
    private static final String EMPTY_KEYWORD = "";

    @Override
    public void removeEsBlog(String id) {
        esBlogRepository.deleteById(id);
    }

    @Override
    public EsBlog updateEsBlog(EsBlog esBlog) {
        return esBlogRepository.save(esBlog);
    }

    @Override
    public EsBlog getEsBlogByBlogId(Long blogId) {
        return esBlogRepository.findByBlogId(blogId);
    }

    @Override
    public Page<EsBlog> listNewestEsBlogs(String keyword, Pageable pageable) {
        Page<EsBlog> pages;
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        pages = esBlogRepository.findDistinctEsBlogByTitleContainingOrSummaryContainingOrContentContainingOrTagsContaining(keyword, keyword, keyword, keyword, pageable);
        return pages;
    }

    @Override
    public Page<EsBlog> listHottestEsBlogs(String keyword, Pageable pageable) {
        Sort sort = new Sort(Sort.Direction.DESC, "readSize", "commentSize", "voteSize", "createTime");
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        return esBlogRepository.findDistinctEsBlogByTitleContainingOrSummaryContainingOrContentContainingOrTagsContaining(keyword, keyword, keyword, keyword, pageable);
    }

    @Override
    public Page<EsBlog> listEsBlogs(Pageable pageable) {
        return esBlogRepository.findAll(pageable);
    }

    @Override
    public List<EsBlog> listTop5NewestEsBlogs() {
        Page<EsBlog> page = this.listNewestEsBlogs(EMPTY_KEYWORD, TOP_5_PAGEABLE);
        return page.getContent();
    }

    @Override
    public List<EsBlog> listTop5HottestEsBlogs() {
        Page<EsBlog> page = this.listHottestEsBlogs(EMPTY_KEYWORD, TOP_5_PAGEABLE);
        return page.getContent();
    }

    @Override
    public List<TagVO> listTop30Tags() {
        List<TagVO> list = new ArrayList<>();
        SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(matchAllQuery()).withSearchType(SearchType.QUERY_THEN_FETCH).withIndices("blog").withTypes("blog").addAggregation(terms("tags").field("tags").order(Terms.Order.count(false)).size(30)).build();
        Aggregations aggregations = elasticsearchTemplate.query(searchQuery, SearchResponse::getAggregations);
        StringTerms modelTerms = (StringTerms) aggregations.asMap().get("tags");
        Iterator<StringTerms.Bucket> modelBucketIt = modelTerms.getBuckets().iterator();
        while (modelBucketIt.hasNext()) {
            Terms.Bucket actionTypeBucket = modelBucketIt.next();
            list.add(new TagVO(actionTypeBucket.getKey().toString(), actionTypeBucket.getDocCount()));
        }
        return list;
    }

    @Override
    public List<User> listTop12Users() {
        List<String> usernameList = new ArrayList<>();
        SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(matchAllQuery()).withSearchType(SearchType.QUERY_THEN_FETCH).withIndices("blog").withTypes("blog").addAggregation(terms("users").field("username").order(Terms.Order.count(false)).size(12)).build();
        Aggregations aggregations = elasticsearchTemplate.query(searchQuery, SearchResponse::getAggregations);
        StringTerms modelTerms = (StringTerms) aggregations.asMap().get("users");
        Iterator<StringTerms.Bucket> modelBucketIt = modelTerms.getBuckets().iterator();
        while (modelBucketIt.hasNext()) {
            Terms.Bucket actionTypeBucket = modelBucketIt.next();
            String username = actionTypeBucket.getKey().toString();
            usernameList.add(username);
        }
        return userService.listUsersByUserNames(usernameList);
    }
}
