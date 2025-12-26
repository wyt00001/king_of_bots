<template>
  <div class="container">
    <div class="row">
      <div class="col-3">
        <div class="card" style="margin-top:20px">
          <div class="card-body">
            <img :src="$store.state.user.photo" alt="" style="width:100%;">

          </div>
        </div>
      </div>
      <div class="col-9">
        <div class="card" style="margin-top:20px">
          <div class="card-header">
            <span style="font-size:120%">我的Bot</span>
            <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#add-bot-btn"
              style="float:right;">创建Bot</button>


            <div class="modal fade" id="add-bot-btn" tabindex="-1" aria-labelledby="exampleModalLabel"
              aria-hidden="true">
              <div class="modal-dialog modal-lg"> <!-- 或 modal-xl -->
                <div class="modal-content">
                  <div class="modal-header">
                    <h1 class="modal-title fs-5">创建Bot</h1>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                  </div>
                  <div class="modal-body">
                    <div class="mb-3">
                      <label for="add-bot-title" class="form-label">名称</label>
                      <input v-model="botadd.title" type="text" class="form-control" id="add-bot-title"
                        placeholder="请输入Bot名称">
                    </div>
                    <div class="mb-3">
                      <label for="add-bot-desc" class="form-label">简介</label>
                      <textarea v-model="botadd.description" class="form-control" id="add-bot-desc" rows="3"
                        placeholder="请输入Bot简介"></textarea>
                    </div>
                    <div class="mb-3">
                      <label for="add-bot-code" class="form-label">代码</label>
                      <VAceEditor v-model:value="botadd.content" @init="editorInit" lang="c_cpp" theme="textmate"
                        style="height: 300px" :options="{
                          enableBasicAutocompletion: true, //启用基本自动完成
                          enableSnippets: true, // 启用代码段
                          enableLiveAutocompletion: true, // 启用实时自动完成
                          fontSize: 14, //设置字号
                          tabSize: 2, // 标签大小
                          showPrintMargin: false, //去除编辑器里的竖线
                          highlightActiveLine: true,
                        }" />
                    </div>
                  </div>
                  <div class="modal-footer">
                    <div class="error-message">{{ botadd.error_message }}</div>
                    <button type="button" @click="add_bot" class="btn btn-primary">创建</button>
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消</button>
                  </div>
                </div>
              </div>
            </div>


            <div class="card-body">
              <table class="table table-striped table-hover">
                <thead>
                  <tr>
                    <th>
                      Bot名称
                    </th>
                    <th>
                      创建时间
                    </th>
                    <th>
                      操作
                    </th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="bot in bots" :key="bot.id">
                    <td>
                      {{ bot.title }}
                    </td>
                    <td>
                      {{ bot.createtime }}
                    </td>
                    <td>
                      <button type="button" class="btn btn-secondary" style="margin-right:10px" data-bs-toggle="modal"
                        :data-bs-target="'#update-bot-model-' + bot.id" @click="openEditModal(bot)">修改</button>
                      <button type="button" class="btn btn-danger" @click="remove_bot(bot)">删除</button>
                      <div class="modal fade" :id="'update-bot-model-' + bot.id" tabindex="-1"
                        aria-labelledby="exampleModalLabel" aria-hidden="true"> <!--写表达式后面要加冒号-->
                        <div class="modal-dialog modal-lg"> <!-- 或 modal-xl -->
                          <div class="modal-content">
                            <div class="modal-header">
                              <h1 class="modal-title fs-5">修改Bot</h1>
                              <button type="button" class="btn-close" data-bs-dismiss="modal"
                                aria-label="Close"></button>
                            </div>
                            <div class="modal-body">
                              <div class="mb-3">
                                <label for="update-bot-title" class="form-label">名称</label>
                                <input v-model="botEdits.get(bot.id).title" type="text" class="form-control"
                                  id="add-bot-title" placeholder="请输入Bot名称">
                              </div>
                              <div class="mb-3">
                                <label for="update-bot-desc" class="form-label">简介</label>
                                <textarea v-model="botEdits.get(bot.id).description" class="form-control"
                                  id="add-bot-desc" rows="3" placeholder="请输入Bot简介"></textarea>
                              </div>
                              <div class="mb-3">
                                <label for="update-bot-code" class="form-label">代码</label>
                                <VAceEditor v-model:value="botEdits.get(bot.id).content" @init="editorInit" lang="c_cpp"
                                  theme="textmate" style="height: 300px" :options="{
                                    enableBasicAutocompletion: true, //启用基本自动完成
                                    enableSnippets: true, // 启用代码段
                                    enableLiveAutocompletion: true, // 启用实时自动完成
                                    fontSize: 14, //设置字号
                                    tabSize: 2, // 标签大小
                                    showPrintMargin: false, //去除编辑器里的竖线
                                    highlightActiveLine: true,
                                  }" />

                              </div>
                            </div>
                            <div class="modal-footer">
                              <div class="error-message">{{ botEdits.get(bot.id).error_message }}</div>
                              <button type="button" @click="update_bot(bot)" class="btn btn-primary">保存修改</button>
                              <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消</button>
                            </div>
                          </div>
                        </div>
                      </div>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, reactive } from 'vue'
import $ from 'jquery'
import { useStore } from 'vuex'
import { Modal } from 'bootstrap/dist/js/bootstrap'
import { VAceEditor } from 'vue3-ace-editor';
import ace from 'ace-builds';
import 'ace-builds/src-noconflict/mode-json';
import 'ace-builds/src-noconflict/theme-chrome';
import 'ace-builds/src-noconflict/ext-language_tools';
import 'ace-builds/src-noconflict/mode-c_cpp';

export default {
  components: {
    VAceEditor
  },
  setup() {
    ace.config.set(
      "basePath",
      "https://cdn.jsdelivr.net/npm/ace-builds@" + require('ace-builds').version + "/src-noconflict/")
    const store = useStore()
    let bots = ref([]);
    const botadd = reactive({
      title: '',
      description: '',
      content: '',
      error_message: '',
    }) //const 让botadd不能被引用为别的对象，但是对象自己的属性可以被修改
    const createBotEdit = () => reactive({
      title: '',
      description: '',
      content: '',
      error_message: '',
    });
    const botEdits = reactive(new Map());
    const openEditModal = (bot) => {
      if (!botEdits.has(bot.id)) {
        botEdits.set(bot.id, createBotEdit());
      }
      const edit = botEdits.get(bot.id);
      edit.title = bot.title;
      edit.description = bot.description;
      edit.content = bot.content;
      edit.error_message = '';
    };
    const refresh_bots = () => {
      $.ajax({
        url: 'https://app7634.acapp.acwing.com.cn/api/user/bot/getlist/',
        type: 'GET',
        headers: {
          Authorization: 'Bearer ' + store.state.user.token
        },
        success: (resp) => {
          bots.value = resp
          resp.forEach(bot => {
            if (!botEdits.has(bot.id)) {
              botEdits.set(bot.id, createBotEdit());
            }
          });
        }
      })
    }
    refresh_bots()
    const add_bot = () => {
      $.ajax({
        url: 'https://app7634.acapp.acwing.com.cn/api/user/bot/add/',
        type: 'POST',
        headers: {
          Authorization: 'Bearer ' + store.state.user.token //security config里没有放行的都需要加上这个header
        },
        data: {
          title: botadd.title,
          description: botadd.description,
          content: botadd.content
        },
        success: (resp) => {
          if (resp.error_message === 'success') {
            botadd.title = ''
            botadd.description = ''
            botadd.content = ''
            Modal.getInstance('#add-bot-btn').hide()//id前要加#,class前要加.
            refresh_bots();
          } else {
            botadd.error_message = resp.error_message
            console.log(resp.error_message)
          }
        }
      })
    }
    const update_bot = (bot) => {
      const edit = botEdits.get(bot.id);
      $.ajax({
        url: 'https://app7634.acapp.acwing.com.cn/api/user/bot/update/',
        type: 'POST',
        headers: {
          Authorization: 'Bearer ' + store.state.user.token
        },
        data: {
          bot_id: bot.id,
          title: edit.title,
          description: edit.description,
          content: edit.content
        },
        success: (resp) => {
          if (resp.error_message === 'success') {
            // 成功后再更新原数据
            bot.title = edit.title;
            bot.description = edit.description;
            bot.content = edit.content;
            Modal.getInstance('#update-bot-model-' + bot.id).hide();
            refresh_bots();
          } else {
            edit.error_message = resp.error_message;
          }
        }
      });
    };



    const remove_bot = (bot) => {
      $.ajax({
        url: 'https://app7634.acapp.acwing.com.cn/api/user/bot/remove/',
        type: 'post',
        data: {
          bot_id: bot.id
        },
        headers: {
          Authorization: 'Bearer ' + store.state.user.token
        },
        success: (resp) => {
          if (resp.error_message === 'success') {
            refresh_bots()
            console.log('删除成功')
          }
        }
      })
    }

    return {
      bots,
      botadd,
      add_bot,
      update_bot,
      remove_bot,
      openEditModal,
      botEdits,
    }
  }
}
</script>

<style scoped>
div.error-message {
  color: red;
}
</style>