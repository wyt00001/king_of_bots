<template>
    <div ref="parent" class="gamemap">
        <canvas ref="canvas" tabindex="0">
        </canvas>
    </div>
    <!-- ref就是标记，相当于label。DOM（Document Object Model）
就是浏览器把 HTML 文档 解析成的一棵 可编程的对象树。 -->
</template>
<script>
import { GameMap } from '@/assets/scripts/GameMap';
import { ref, onMounted } from "vue";
import { useStore } from "vuex";
export default {
    setup() {
        const store = useStore();
        let parent = ref(null);
        let canvas = ref(null);
        onMounted(() => {
            store.commit("updateGameObject", new GameMap(canvas.value.getContext('2d'), parent.value,store));
        });
        return { parent, canvas };
    }
}
</script>

<style scoped>
div.gamemap {
    width: 100%;
    height: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
}
</style>