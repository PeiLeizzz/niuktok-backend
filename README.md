# niuktok-backend
> 第二届七牛云 1024 创作节参赛作品后端

## 部署

- 依赖
    - Java >= 11
    - Maven >= 3
- 需先启动中间件
- 配置 
    - Nacos 配置需要根据 `config/nacos` 目录中的 yaml 文件配置好
        - `niuktok-datasource-${env}.yaml` 中的 MySQL 数据库地址、认证信息等需要根据实际情况修改
        - `redis-datasource-${env}.yaml` 中的 Redis 缓存地址、认证信息等需要根据实际情况修改
    - 每个微服务的 `bootstrap-${env}.yml` 中的 `spring.cloud.nacos` 地址、认证信息等需要根据实际情况修改
- 运行 Docker 化部署脚本

    ```sh
    # 手动部署时可以将 deploy.sh 中的 CI_BUILD_REF_NAME 设置为 dev / master
    # maven 仓库地址根据实际情况修改
    sudo bash scripts/deploy.sh
    ```

## 访问

- 后端地址：`http://${ip}:${gateway.port}`，默认 `dev` 环境下是 `28001`，可以在 nacos 中进行配置