

export default {
    state: {
        is_record: false,
        a_step: "",
        b_step: "",
        record_loser: "",
    },
    getters: {

    },
    mutations: {
        updateRecordLoser(state, loser) {
            state.loser = loser;
        },
        updateIsRecord(state, is_record) {
            state.is_record = is_record;
        },
        updateSteps(state, data) {
            state.a_steps = data.a_steps;
            state.b_steps = data.b_steps;
        },
        updateUserInfo(state, data){
            state.a_username = data.a_username;
            state.b_username = data.b_username;
            state.a_photo = data.a_photo;
            state.b_photo = data.b_photo;
        }
    },
    actions: {
    },
    modules: {

    }
}