package atd.code.authority.entity;

public class Authority {
    Integer id;
    String roleid;
    String appid;
    String funcid;
    String permission;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRoleid() {
        return roleid;
    }

    public void setRoleid(String roleid) {
        this.roleid = roleid;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getFuncid() {
        return funcid;
    }

    public void setFuncid(String funcid) {
        this.funcid = funcid;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    @Override
    public String toString() {
        return "Authority{" +
                "id=" + id +
                ", roleid='" + roleid + '\'' +
                ", appid='" + appid + '\'' +
                ", funcid='" + funcid + '\'' +
                ", permission='" + permission + '\'' +
                '}';
    }
}
