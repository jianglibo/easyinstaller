package com.jianglibo.vaadin.dashboard;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import com.google.common.base.Strings;
import com.google.common.net.MediaType;

public abstract class MvcBase extends Tbase {

    protected void fetchInternal(String url, //
            String projection, //
            RequestPostProcessor auth, //
            ResultHandler rh, //
            ResultMatcher... matchers) throws Exception {
        fetchInternal(url, projection, auth, rh, false, matchers);
    }

    protected void fetchInternal(String url, //
            String projection, //
            RequestPostProcessor auth, //
            ResultHandler rh, boolean skipStatus, //
            ResultMatcher... matchers) throws Exception {
        ResultActions ra = fetchBeforeAndDo(url, projection, auth,skipStatus, matchers);
        matchAndDo(rh, ra, matchers);
    }
    
    protected void createInternal(String url, //
            String content, //
            RequestPostProcessor auth, //
            ResultHandler rh, //
            ResultMatcher... matchers) throws Exception {
        createInternal(url, content, auth, rh, false, matchers);
    }


    protected void createInternal(String url, //
            String content, //
            RequestPostProcessor auth, //
            ResultHandler rh, //
            boolean skipStatus, //
            ResultMatcher... matchers) throws Exception {
        ResultActions ra = createBeforeAndDo(url, content, auth, skipStatus, matchers);

        matchAndDo(rh, ra, matchers);
    }

    protected ResultActions createBeforeAndDo(String url, //
            String content, //
            RequestPostProcessor auth, //
            boolean skipStatus, //
            ResultMatcher... matchers) throws Exception {
        return cuBeforeAndDo(post(url), url, content, auth,skipStatus, matchers);
    }
    
    protected void updateInternal(String url, //
            String content, //
            RequestPostProcessor auth, //
            ResultHandler rh, //
            ResultMatcher... matchers) throws Exception {
        updateInternal(url, content, auth, rh, false, matchers);
    }


    protected void updateInternal(String url, //
            String content, //
            RequestPostProcessor auth, //
            ResultHandler rh, //
            boolean skipStatus, //
            ResultMatcher... matchers) throws Exception {
        ResultActions ra = updateBeforeAndDo(url, content, auth,skipStatus, matchers);

        matchAndDo(rh, ra, matchers);
    }

    protected ResultActions updateBeforeAndDo(String url, //
            String content, //
            RequestPostProcessor auth, //
            boolean skipStatus, //
            ResultMatcher... matchers) throws Exception {
        return cuBeforeAndDo(put(url), url, content, auth,skipStatus, matchers);
    }

    protected ResultActions fetchBeforeAndDo(String url, //
            String projection, //
            RequestPostProcessor auth, //
            boolean skipStatus, //
            ResultMatcher... matchers) throws Exception {
        MockHttpServletRequestBuilder rhrb = get(url);
        if (!Strings.isNullOrEmpty(projection)) {
            rhrb.param("projection", projection);//
        }
        if (auth != null) {
            rhrb.with(auth);
        }

        if (matchers.length == 0 && !skipStatus) {
            matchers = new ResultMatcher[] { status().is2xxSuccessful() };
        }

        ResultActions ra = mvc.perform(rhrb//
                .contentType(MediaType.JSON_UTF_8.toString())//
                .accept(MediaType.JSON_UTF_8.toString()));
        return ra;
    }

    public abstract String getPlural();

    public String getOneItemUrl(long id) {
        return getOneItemUrl(String.valueOf(id));
    }

    public String getOneItemUrl(String id) {
        return getPluralPath() + "/" + id;
    }

    public String getOneItemUrlQ(long id) {
        return getOneItemUrlQ(String.valueOf(id));
    }

    public String getOneItemUrlQ(String id) {
        return getPluralPath() + "?id=" + id;
    }

    public String getPluralPath() {
        return getRestUri(getPlural());
    }

    public String getUpdateUrl(String id) {
        return getOneItemUrl(id) + "/update";
    }

    public String getUpdateUrl(long id) {
        return getUpdateUrl(String.valueOf(id));
    }

    private void matchAndDo(ResultHandler rh, ResultActions ra, ResultMatcher... matchers) throws Exception {
        for (ResultMatcher rm : matchers) {
            ra = ra.andExpect(rm);
        }
        if (rh != null) {
            ra.andDo(rh);
        }
    }

    private ResultActions cuBeforeAndDo(MockHttpServletRequestBuilder rhrb, String url, String content, RequestPostProcessor auth, boolean skipStatus, ResultMatcher... matchers)
            throws Exception {
        if (auth != null) {
            rhrb.with(auth);
        }

        if (matchers.length == 0 && !skipStatus) {
            matchers = new ResultMatcher[] { status().is2xxSuccessful() };
        }

        ResultActions ra = mvc.perform(rhrb//
                .contentType(MediaType.JSON_UTF_8.toString())//
                .content(content) //
                .accept(MediaType.JSON_UTF_8.toString()));
        return ra;
    }

}
