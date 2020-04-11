package com.company.project.business.consts;

/**
 * 项目常量
 */
public final class ProjectConstant {
    public static final String BASE_PACKAGE = "com.company.projects";//生成代码所在的基础包名称，可根据自己公司的项目修改（注意：这个配置修改之后需要手工修改src目录项目默认的包路径，使其保持一致，不然会找不到类）

    public static final String MODEL_PACKAGE = "persistence.beans";//生成的Model所在包
    public static final String MAPPER_PACKAGE = "persistence.mapper";//生成的Mapper所在包
    public static final String MAPPER_XML_PACKAGE =  "persistence.xml";//生成的Mapper所在包
    public static final String SERVICE_PACKAGE =   "business.service";//生成的Service所在包
    public static final String SERVICE_IMPL_PACKAGE =  "business.service.impl";//生成的ServiceImpl所在包
    public static final String CONTROLLER_PACKAGE = "controller";//生成的Controller所在包

    public static final String MAPPER_INTERFACE_REFERENCE = BASE_PACKAGE + ".plugin.BaseMapper";//Mapper插件基础接口的完全限定名


    /**
     * 安全密码(UUID生成)，作为盐值用于用户密码的加密
     */
    public static final String APP_SECURITY_KEY = "929123f8f17944e8b0a531045453e1f1";
}
