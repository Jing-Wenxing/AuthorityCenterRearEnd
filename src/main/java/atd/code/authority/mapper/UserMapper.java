package atd.code.authority.mapper;

import atd.code.authority.entity.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {

    /**
     * ===== 信息创建 =====
     */

    /**
     * 创建用户信息
     */
    Integer createBaseInfo(BaseInfo baseInfo);

    /**
     * 创建个人信息
     */
    Integer createUserInfo(UserInfo userInfo);

    /**
     * 创建角色信息(学生)
     */
    Integer createRoleInfoOfStudent(StudentInfo studentInfo);

    /**
     * 创建角色信息(教师)
     */
    Integer createRoleInfoOfTeacher(TeacherInfo teacherInfo);

    /**
     * 创建角色信息(社团)
     */
    Integer createRoleInfoLeague(LeagueInfo leagueInfo);

    /**
     * ===== 信息设置 =====
     */

    /**
     * 设置用户信息
     */
    Integer setBaseInfo(BaseInfo baseInfo);

    /**
     * 设置个人信息
     */
    Integer setUserInfo(UserInfo userInfo);

    /**
     * 设置密码
     */
    Integer setPassword(BaseInfo baseInfo);

    /**
     * 设置角色信息(学生)
     */
    Integer setRoleStudent(StudentInfo studentInfo);

    /**
     * 设置角色信息(教师)
     */
    Integer setRoleTeacher(TeacherInfo teacherInfo);

    /**
     * 设置角色信息(社团)
     */
    Integer setRoleLeague(LeagueInfo leagueInfo);

    /**
     * ===== UUID获取 =====
     */

    /**
     * 账户名获取UUID
     */
    String getAccountToUUID(String account);

    /**
     * 手机号获取UUID
     */
    String getPhoneToUUID(String telephone);

    /**
     * UUID获取用户信息
     */
    BaseInfo getUUIDToBaseInfo(String uuid);

    /**
     * UUID获取个人信息
     */
    UserInfo getUUIDToUserInfo(String uuid);

    /**
     * UUID获取角色信息(学生)
     */
    StudentInfo getUUIDToStudentInfo(String uuid);

    /**
     * UUID获取角色信息(教师)
     */
    TeacherInfo getUUIDToTeacherInfo(String uuid);

    /**
     * UUID获取角色信息(社团)
     */
    LeagueInfo getUUIDToLeagueInfo(String uuid);

    /**
     * ===== 重复性校验 =====
     */

    /**
     * 账户名重复
     */
    Integer accountCheck(String account);

    /**
     * 手机号重复
     */
    Integer phoneCheck(String telephone);

    /**
     * 姓名重复
     */
    Integer fullNameCheck(String fullName);

    /**
     * ===== 权限信息 =====
     */

    List<Authority> getUUIDAuthority(String roleid);
}
