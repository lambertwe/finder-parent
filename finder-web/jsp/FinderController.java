/**
 * sprint mvc 集成
 */
@Controller
public class FinderController implements InitializingBean {
    @Resource
    private ServletContext servletContext;
    private com.skin.finder.web.ActionDispatcher dispatcher;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.dispatcher = new com.skin.finder.web.ActionDispatcher();
        this.dispatcher.setPackages(new String[]{"com.skin.finder.servlet", "com.skin.finder.admin.servlet"});
        this.dispatcher.init(this.servletContext);
    }

    /**
     * 凡是集成到其他MVC框架中的, finder的集群模式可能会失效。
     * 要在集成模式下安全的使用finder的集群模式，请使用第一种方式集成，即配置servlet的方式。
     * 并且要将servlet配置到最前面，确保在其他mvc框架之前执行。
     */
    @RequestMapping(value = "/finder")
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String loginURL = com.skin.finder.filter.SessionFilter.getLoginURL(request);

        /**
         * 若要取消鉴权, 放开此处的代码, 注入管理员帐号
         * 所有用户使用同一账号登录, 该账号必须是有效的用户帐号
         * LoginServlet.login(request, response, "admin");
         */

        if(com.skin.finder.filter.SessionFilter.check(request, response, loginURL)) {
            this.dispatcher.service(request, response);
        }
    }
}
