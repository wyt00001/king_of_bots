const { defineConfig } = require('@vue/cli-service')

module.exports = defineConfig({
  transpileDependencies: true,

  devServer: {
    port: 8081,          // 可选，固定端口
    proxy: {
      '/api': {
        target: 'https://app7634.acapp.acwing.com.cn/', // 后端地址
        changeOrigin: true               // 虚拟主机站点必备
      }
    }
  }
})