<template>
    <div class="playground">
        <div class="player left-player">
            <img :src="store.state.record.a_photo" alt="" />
            <div>{{ store.state.record.a_username }}</div>
        </div>

        <GameMap />

        <div class="player right-player">
            <img :src="store.state.record.b_photo" alt="" />
            <div>{{ store.state.record.b_username }}</div>
        </div>
    </div>
</template>

<script>
import GameMap from './GameMap.vue'
import { useStore } from "vuex";

export default {
    components: { GameMap },
    setup() {
        const store = useStore();
        return { store };
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