package atd.code.authority.entity;

public class RoleInfo {
    String uuid;
    String account;
    String role;
    // 学生身份
    String school_id;
    String professional;
    Integer grade;
    Integer classes;
    // 教师身份
    String work_id;
    String office;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getSchool_id() {
        return school_id;
    }

    public void setSchool_id(String school_id) {
        this.school_id = school_id;
    }

    public String getProfessional() {
        return professional;
    }

    public void setProfessional(String professional) {
        this.professional = professional;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public Integer getClasses() {
        return classes;
    }

    public void setClasses(Integer classes) {
        this.classes = classes;
    }

    public String getWork_id() {
        return work_id;
    }

    public void setWork_id(String work_id) {
        this.work_id = work_id;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    @Override
    public String toString() {
        return "RoleInfo{" +
                "uuid='" + uuid + '\'' +
                ", account='" + account + '\'' +
                ", role='" + role + '\'' +
                ", school_id='" + school_id + '\'' +
                ", professional='" + professional + '\'' +
                ", grade=" + grade +
                ", classes=" + classes +
                ", work_id='" + work_id + '\'' +
                ", office='" + office + '\'' +
                '}';
    }
}
