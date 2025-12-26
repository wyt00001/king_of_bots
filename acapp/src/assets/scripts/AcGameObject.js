const AC_GAME_OBJECTS = [];

export class AcGameObject {
    constructor() {
        AC_GAME_OBJECTS.push(this); //把元素放在数组最后
        this.timedelta = 0; //当前帧距离上一帧的时间间隔，单位是秒
        this.has_called_start = false; //是否执行过start函数
    }

    start() {  //只执行一次

    }

    update() {  //每一帧执行一次，除了第一帧

    }

    on_destroy() {  //销毁对象时执行一次

    }

    destroy() {  //销毁对象
        this.on_destroy(); 

        for (let i = 0; i < AC_GAME_OBJECTS.length; i++) {
            if (AC_GAME_OBJECTS[i] === this) {
                AC_GAME_OBJECTS.splice(i, 1);
                break;
            }
        }
    }
}

let last_timestamp = 0;

const step = (timestamp) => {
    for (let obj of AC_GAME_OBJECTS) 
        if (!obj.has_called_start) {
            obj.start();
            obj.has_called_start = true;
        } else {
            obj.timedelta = (timestamp - last_timestamp) / 1000; //转换成秒
            obj.update();
        }
        last_timestamp = timestamp;
        requestAnimationFrame(step); //下一帧执行step函数
    }
    requestAnimationFrame(step);