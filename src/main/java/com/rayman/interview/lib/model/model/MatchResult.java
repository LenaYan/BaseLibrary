package com.rayman.interview.lib.model.model;

import java.util.List;

public class MatchResult {

    private boolean isMatch;

    private List<String> groupList;

    public boolean isMatch() {
        return isMatch;
    }

    public void setMatch(boolean match) {
        isMatch = match;
    }

    public List<String> getGroupList() {
        return groupList;
    }

    public void setGroupList(List<String> groupList) {
        this.groupList = groupList;
    }

}
