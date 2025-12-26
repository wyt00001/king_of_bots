import $ from 'jquery'

export default { //文件默认导出的对象，就是这个模块的内容
    state: {
        id: "",
        username: "",
        photo: "",
        token: "",
        is_login: false,
        pulling_info: true
    },
    getters: {
    },
    mutations: { //mutations用来同步修改state里的值，action用来异步，返回结果后再调用mutations修改state。mutations用commit提交，action用dispatch触发
        updateUser(state, user) {
            state.id = user.id
            state.username = user.username
            state.photo = user.photo
            state.is_login = user.is_login
        },
        updateToken(state, token) {
            state.token = token
        },
        logout(state) {
            state.id = ""
            state.username = ""
            state.photo = ""
            state.token = ""
            state.is_login = false
        },
        updatePullingInfo(state, pulling_info) {
            state.pulling_info = pulling_info
        }
    },
    actions: { //立刻完成的就是同步，需要等待结果的就是异步。同步如纯赋值，数组操作，对象展开，异步如发请求，定时器，Promise.all
        login(context, data) {
            $.ajax({
                url: 'https://app7634.acapp.acwing.com.cn/api/user/account/token/',
                type: 'post',
                data: {
                    username: data.username,
                    password: data.password,
                },
                success(resp) {
                    if (resp.error_message === 'success') {
                        localStorage.setItem('jwt_token', resp.token);
                        context.commit('updateToken', resp.token);
                        // context.dispatch('getinfo', {
                        //     success(resp) {
                        //         console.log(resp)
                        //     },
                        //     error(resp) {
                        //         console.log(resp)
                        //     }
                        // });
                        data.success(resp);
                    }
                    else {
                        data.error(resp);
                    }
                },
                error(resp) {
                    data.error(resp);
                }
            })
        },
        getinfo(context, data) {
            $.ajax({
                url: 'https://app7634.acapp.acwing.com.cn/api/user/account/info/',
                type: 'GET',
                headers: {
                    'Authorization': 'Bearer ' + context.state.token
                },
                success(resp) {
                    if (resp.error_message === 'success') {
                        context.commit("updateUser", {
                            ...resp,// ...是扩展运算符，把对象所有可枚举的属性展开复制到新对象里
                            is_login: true
                        });
                        data.success(resp);
                    }
                    else {
                        data.error(resp);
                    }
                },
                error(resp) {
                    data.error(resp);
                }
            })
        },
        logout(context) {
            localStorage.removeItem('jwt_token');
            context.commit('logout');
        }
    },
    modules: {

    }
}
