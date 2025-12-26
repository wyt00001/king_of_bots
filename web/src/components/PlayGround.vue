<template>
    <div class="playground">
        <div class="player left-player">
            <img :src="leftUser.photo" alt="" />
            <div>{{ leftUser.username }}</div>
        </div>

        <GameMap />

        <div class="player right-player">
            <img :src="rightUser.photo" alt="" />
            <div>{{ rightUser.username }}</div>
        </div>
    </div>
</template>

<script>
import GameMap from './GameMap.vue'
import { useStore } from 'vuex'
import { computed } from 'vue'

export default {
    components: { GameMap },
    setup() {
        const store = useStore()
        const leftUser = computed(() => ({
            username: "left" === store.state.pk.side ? store.state.user.username : store.state.pk.opponent_username,
            photo: "left" === store.state.pk.side ? store.state.user.photo : store.state.pk.opponent_photo
        }))

        const rightUser = computed(() => ({
            username: "right" === store.state.pk.side ? store.state.user.username : store.state.pk.opponent_username,
            photo: "right" === store.state.pk.side ? store.state.user.photo : store.state.pk.opponent_photo
        }))

        return { leftUser, rightUser }
    }
}
</script>

<style scoped>
.playground {
    display: flex;
    justify-content: space-between;
    align-items: center;
    height: 40vw;
    width: 90vh;
    margin: 40px auto;
}

.player {
    display: flex;
    flex-direction: column;
    align-items: center;
    color: white;
    font-weight: bold;
    font-size: 18px;
}

.player img {
    width: 12vh;
    height: 12vh;
    border-radius: 50%;
    margin-bottom: 10px;
    border: 3px solid white;
}

.left-player {
    margin-right: 20px;
}

.right-player {
    margin-left: 20px;
}
</style>