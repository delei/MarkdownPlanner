package org.xumingmingv.markdownplanner.web;

import lombok.Value;

@Value
public class FileVO {
    private String name;
    private String url;
    private boolean booeanDir;

    public String getUrl() {
        return booeanDir ? "/tree" + url  : url;
    }
}
