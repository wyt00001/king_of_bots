import { AcGameObject } from "./AcGameObject";
import { Cell } from "./Cell";

export class Snake extends AcGameObject {
    constructor(info, gamemap) {
        super();

        this.id = info.id;
        this.color = info.color;
        this.gamemap = gamemap;

        this.cells = [new Cell(info.r, info.c, this.color)];
        this.next_cell = null; //下一格位置
        this.speed = 5; //格子/秒
        this.direction = -1; //-1表示没有方向，0上，1右，2下，3左
        this.status = 'idle'; //'idle'静止 'move'移动 'die'死亡

        this.dr = [-1, 0, 1, 0];
        this.dc = [0, 1, 0, -1];

        this.step = 0; //当前回合数
        this.eps = 1e-2; //允许的误差

        this.eye_direction = 0;
        if (this.id === 1) this.eye_direction = 1; //左下角的蛇朝上

        this.eye_dx = [
            [-1, 1],
            [1, 1],
            [1, -1],
            [-1, -1]
        ];
        this.eye_dy = [
            [-1, -1],
            [1, -1],
            [1, 1],
            [-1, 1]
        ];
    }

    start() {

    }

    set_direction(direction) {
        this.direction = direction;
    }

    check_tail_increasing() {
        if (this.step <= 10) return true;
        if (this.step % 3 === 1) return true;
        return false;
    }

    next_step() {
        const d = this.direction;
        this.eye_direction = d;
        this.next_cell = new Cell(this.cells[0].r + this.dr[d], this.cells[0].c + this.dc[d]);
        this.direction = -1; //清空方向
        this.status = 'move';
        this.step++;

        const k = this.cells.length;
        for (let i = k; i > 0; i--) {
            this.cells[i] = JSON.parse(JSON.stringify(this.cells[i - 1]));
        }
    }

    update_move() {
        const dx = this.next_cell.x - this.cells[0].x;
        const dy = this.next_cell.y - this.cells[0].y;
        const distance = Math.sqrt(dx * dx + dy * dy);

        if (distance < this.eps) { //到达下一格
            this.cells[0] = this.next_cell;
            this.next_cell = null;
            this.status = 'idle';
            if (!this.check_tail_increasing()) {
                this.cells.pop();
            }
        } else {
            const move_distance = this.speed * this.timedelta; //移动距离
            this.cells[0].x += move_distance * dx / distance;
            this.cells[0].y += move_distance * dy / distance;

            if (!this.check_tail_increasing()) {
                const k = this.cells.length;
                const tail = this.cells[k - 1];
                const tail_target = this.cells[k - 2];
                const tail_dx = tail_target.x - tail.x;
                const tail_dy = tail_target.y - tail.y;
                tail.x += tail_dx * move_distance / distance;
                tail.y += tail_dy * move_distance / distance;
            }
        }
    }

    update() {
        if (this.status === 'move') {
            this.update_move();
        }
        this.render();
    }

    render() {
        const L = this.gamemap.L;
        const ctx = this.gamemap.ctx;

        ctx.fillStyle = this.color;
        if (this.status === 'die') {
            ctx.fillStyle = 'white';
        }
        for (const cell of this.cells) {
            ctx.beginPath();
            ctx.arc(cell.x * L, cell.y * L, L / 2 * 0.8, 0, 2 * Math.PI);
            ctx.fill();
        }
        for (let i = 1; i < this.cells.length; i++) {
            const a = this.cells[i - 1];
            const b = this.cells[i];
            if (Math.abs(a.x - b.x) < this.eps && Math.abs(a.y - b.y) < this.eps) continue;
            if (Math.abs(a.x - b.x) < this.eps) {
                ctx.fillRect((a.x - 0.4) * L, Math.min(a.y, b.y) * L, L * 0.8, Math.abs(a.y - b.y) * L);
            } else {
                ctx.fillRect(Math.min(a.x, b.x) * L, (a.y - 0.4) * L, Math.abs(a.x - b.x) * L, L * 0.8);
            }
        }
        ctx.fillStyle = 'black';
        for (let i = 0; i < 2; i++) {
            const eye_x = (this.cells[0].x + this.eye_dx[this.eye_direction][i] * 0.15) * L;
            const eye_y = (this.cells[0].y + this.eye_dy[this.eye_direction][i] * 0.15) * L;
            ctx.beginPath();
            ctx.arc(eye_x, eye_y, L * 0.05, 0, 2 * Math.PI);
            ctx.fill();
        }
    }
}

