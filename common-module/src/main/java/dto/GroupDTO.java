package dto;

import java.io.Serializable;
import java.util.List;

public class GroupDTO implements Serializable {

    private List<Long>membersId;
    private String groupName;
    private Long creatorId;
    private String description;

    public GroupDTO() {
    }

    public GroupDTO(List<Long> membersId, String groupName, Long creatorId,String description) {
        this.membersId = membersId;
        this.groupName = groupName;
        this.creatorId = creatorId;
        this.description=description;
    }

    public List<Long> getMembersId() {
        return membersId;
    }

    public void setMembersId(List<Long> membersId) {
        this.membersId = membersId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
       this.groupName = groupName;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
