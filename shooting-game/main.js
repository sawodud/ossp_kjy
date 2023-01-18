//캔버스 세팅
let canvas;
let ctx;
canvas = document.createElement("canvas")
ctx = canvas.getContext("2d")
canvas.width = 600;
canvas.height = 700;
document.body.appendChild(canvas);

let backgroundImage, spaceshipImage, bulletImage, enemyImage, enemyImage2, enemyImage3,
    enemyImage4,enemyBulletImage, enemyBoomImage, lifeImage, gameOverImage, gameClear;
let gameOver = false;
let gameStart = true;
let score = 0;
let life = 0;


function loadImage(){
    backgroundImage = new Image();
    backgroundImage.src = "images/background.gif";

    spaceshipImage = new Image();
    spaceshipImage.src = "images/spaceship.png";

    bulletImage = new Image();
    bulletImage.src = "images/bullet.png";

    enemyImage = new Image();
    enemyImage.src = "images/enemy.png";

    enemyImage2 = new Image();
    enemyImage2.src = "images/enemy1.png";

    enemyImage3 = new Image();
    enemyImage3.src = "images/enemy3.png";

    enemyImage4 = new Image();
    enemyImage4.src = "images/enemy3.png";

    enemyBoomImage = new Image();
    enemyBoomImage.src = "images/boom.png"

    enemyBulletImage = new Image();
    enemyBulletImage.src = "images/enemybullet.png"

    lifeImage = new Image();
    lifeImage.src = "images/spaceship.png"

    gameOverImage = new Image();
    gameOverImage.src = "images/gameover.png"

    gameClear = new Image();
    gameClear.src = "images/클리어.jpg"
} 
let keysDown = {};
function keyboardListener(){
    document.addEventListener('keydown', (event)=>{
        keysDown[event.keyCode] = true;
        // console.log('키다운객체에 들어간 값은?', keysDown)
          if(event.keyCode === 82){
            location.reload()
          }

    })
    document.addEventListener('keyup', (event)=>{
        delete keysDown[event.keyCode];
        // console.log('버튼 때면 없애주세요', keysDown)
        if(gameStart == true){
            if(event.keyCode === 32){
                gameStart = false;
            }
        }
        else{
            if(event.keyCode === 32){
                shootBullet();
            }
        }
    })
}

// 우주선 좌표
let spaceshipX = canvas.width/2-32;
let spaceshipY = canvas.height - 60;

//우주선 이동
const spaceshipMove = () => {
    if(37 in keysDown && spaceshipX > 0){
        spaceshipX += -8;
    } else if(39 in keysDown && spaceshipX < canvas.width-60){
        spaceshipX += 8;
    } else if(38 in keysDown && spaceshipY > 0){
        spaceshipY += -8;
    } else if(40 in keysDown && spaceshipY < canvas.height-60){
        spaceshipY += 8;
    }
}


//총알 생성
let bulletList = [];

class designBullet {
    constructor() {
        this.create = function () {
            this.x = spaceshipX + 25;
            this.y = spaceshipY - 10;
            this.alive = true;

            bulletList.push(this);
        };
        this.attack = function (lifeUp) {
            enemyList.map((enemy, i) => {
                if (this.y - 20 <= enemy.y &&
                    enemy.y <= this.y &&
                    this.x - 20 <= enemy.x &&
                    enemy.x <= this.x
                ) {
                this.alive = false; // 죽은 총알
                // enemy.alive = false;
                score++;
                call();
                call1();
                call3();
                enemyList.splice(i, 1);
                lifeUp(); // 적우주선 격추 후 콜백 함수로 lifeUp함수를 실행시켜준다 
                }
            });
            enemyList2.map((enemy2, i) => {
                if (this.y - 40 <= enemy2.y &&
                    enemy2.y <= this.y &&
                    this.x - 40 <= enemy2.x &&
                    enemy2.x <= this.x   
                ) {
                this.alive = false; // 죽은 총알
                // enemy2.alive = false;
                score++;
                call();
                call1();
                call3();
                enemyList2.splice(i,1);
                lifeUp();
                }
            })
            enemyList3.map((enemy3, i) => {
                if (this.y - 40 <= enemy3.y &&
                    enemy3.y <= this.y &&
                    this.x - 40 <= enemy3.x &&
                    enemy3.x <= this.x   
                ) {
                this.alive = false; // 죽은 총알
                score++
                call();
                call1();
                call3();
                enemyList3.splice(i,1);
                lifeUp();
                }
            })
            enemyList4.map((enemy3, i) => {
                if (this.y - 40 <= enemy3.y &&
                    enemy4.y <= this.y &&
                    this.x - 40 <= enemy3.x &&
                    enemy4.x <= this.x   
                ) {
                this.alive = false; // 죽은 총알
                score++
                call();
                call1();
                call3();
                enemyList4.splice(i,1);
                lifeUp();
                }
            })
        };
    }
}


//총알 발사
function shootBullet(){
    let bullet = new designBullet();
    // console.log('bullet: ', bullet);
    
    bullet.create();
}

//총알 움직임
const bulletMove = () => {
    bulletList.map((v,i)=>{
        if(v.y === 0 ) {
            bulletList.splice(i,1)
        } else {
            v.y += -15
        }
    })
}

//적 생성 
let enemyList = [];

class designEnemy {
    constructor() {
        this.x = Math.floor(Math.random() * 570);
        this.y = 0;
        this.alive = true;
        this.create = function () {
            enemyList.push(this);
        };
    }
}

function createEnemy(){
    let enemy = new designEnemy();
    enemy.create();
    call3();
}
setInterval(() => 
    createEnemy()
, 1000)

function call3(){
    if(score /10 == 1){
        setInterval(() => createEnemy(), 700)
    }else if(score /10 == 2){
        setInterval(() => createEnemy(), 600)
    }else if(score /10 == 3){
        setInterval(() => createEnemy(), 500)
    }else if(score /10 == 4){
        setInterval(() => createEnemy(), 400)
    }
}

//적3 생성
let enemyList3 = [];

class designEnemy3 {
    constructor() {
        this.x = Math.floor(Math.random() * 550);
        this.y = Math.floor(Math.random() * 100);
        this.speed = 3;
        this.radius = 25;
        this.alive = true;
        
        this.create = function () {
            enemyList3.push(this);
        };
    }
}
function call(){
    if(score % 5 == 0 ){
        createEnemy3();
    } 
}


function createEnemy4(){
    let enemy3 = new designEnemy3();
    enemy3.create();
}

//적4 생성
let enemyList4 = [];

class designEnemy4 {
    constructor() {
        this.x = Math.floor(Math.random() * 550);
        this.y = Math.floor(Math.random() * 100);
        this.speed = 3;
        this.radius = 25;
        this.alive = true;
        
        this.create = function () {
            enemyList4.push(this);
        };
    }
}
function call1(){
    if(score > 30 ){
        if(score % 10 == 3)
        createEnemy4();
    } 
}


function createEnemy3(){
    let enemy3 = new designEnemy3();
    enemy3.create();
}


//적2 생성
let enemyList2 = [];
let enemyBulletList = [];

class designEnemy2 {
    constructor() {
        this.x = Math.floor(Math.random() * 550);
        this.y = Math.floor(Math.random() * 100);
        this.speed = 3;
        this.radius = 25;
        this.alive = true;
        
        this.create = function () {
            enemyList2.push(this);
        };
    }
}

class designEnemyBullet {
    constructor() {
        this.create = function (enemyX, enemyY) {
            this.x = enemyX;
            this.y = enemyY;
            this.alive = true;

            enemyBulletList.push(this)
        }
    }
}

//적2 총알 발사
function createEnemyBullet(enemyX, enemyY){
    let enemyBullet = new designEnemyBullet();
    enemyBullet.create(enemyX, enemyY);
}

function createEnemyBullet2(){
    for (let i=0; i<enemyList2.length; i++){
        if(enemyList2[i].alive){
            createEnemyBullet(enemyList2[i].x + 15, enemyList2[i].y + 25)
        }
    }
}
        
setInterval(() => {
    createEnemyBullet2()
}, 500);
    


//적 총알 움직임
const enemyBulletMove = () => {
    enemyBulletList.map((v,i) => {
        if(v.y > canvas.height) {
            enemyBulletList.splice(i,1)
        } else {
            v.y += 5
        }
    })
}
    
function createEnemy2(){
    let enemy2 = new designEnemy2();
    enemy2.create();
}
setInterval(() => 
    createEnemy2()
, 5000);




//적 이동
const moveEnemy = () => {
    enemyList.map((enemy,i)=>{
        if(enemy.y >= 690) {
            enemyList.splice(i,1)
        } else{
            enemy.y += 5
        }
    })
}

const moveEnemy2 = () => {
    enemyList2.map((enemy)=>{
        if(enemy.x <= 0 || enemy.x >= canvas.width-40){
            enemy.speed *= -1; 
            enemy.x += enemy.speed;
        } else {
            enemy.x += enemy.speed;
        }
        
    })
}
let dx = 3
let dy = -3

const moveEnemy3 = () => {
    enemyList3.map((enemy)=>{

        if (enemy.y <= 0 || enemy.y >= canvas.height-40) {
            dy *= -1
            enemy.y += dy
        }else{
            enemy.x += dx
            enemy.y += dy
        }
        if (enemy.x <= 0 || enemy.x >= canvas.width-40) {
            dx *= -1
            enemy.x += dx
        }else{
            enemy.x += dx
            enemy.y += dy
        }   

    })
}

const moveEnemy4 = () => {
    enemyList4.map((enemy)=>{

        if (enemy.y <= 0 || enemy.y >= canvas.height-40) {
            dy *= -1
            enemy.y += dy
        }else{
            enemy.x += dx
            enemy.y += dy
        }
        if (enemy.x <= 0 || enemy.x >= canvas.width-40) {
            dx *= -1
            enemy.x += dx
        }else{
            enemy.x += dx
            enemy.y += dy
        }   

    })
}

//life 증감 (적우주선이랑 부딛히면 -1 score 높아지면 + 1)
const lifeDown = (GameOver) => {
    enemyList.map((enemy, i)=>{
        if (
            enemy.y - 30 <= spaceshipY &&
            spaceshipY <= enemy.y &&
            enemy.x - 45 <= spaceshipX  &&
            spaceshipX <= enemy.x + 15 
        ) {
            life += -1;
            enemyList.splice(i,1)
            
            if(life < 0) {GameOver()}
        } 
    })
    enemyList3.map((enemy, i)=>{
        if (
            enemy.y - 30 <= spaceshipY &&
            spaceshipY <= enemy.y &&
            enemy.x - 45 <= spaceshipX  &&
            spaceshipX <= enemy.x + 15 
        ) {
            life += -1;
            enemyList.splice(i,1)
            
            if(life < 0) {GameOver()}
        } 
    })
    enemyList4.map((enemy, i)=>{
        if (
            enemy.y - 30 <= spaceshipY &&
            spaceshipY <= enemy.y &&
            enemy.x - 45 <= spaceshipX  &&
            spaceshipX <= enemy.x + 15 
        ) {
            life += -1;
            enemyList.splice(i,1)
            
            if(life < 0) {GameOver()}
        } 
    })
    enemyBulletList.map((Ebullet, i)=>{
        if( Ebullet.y - 40 <= spaceshipY &&  
            spaceshipY <= Ebullet.y &&
            Ebullet.x - 40 <= spaceshipX &&
            spaceshipX <= Ebullet.x   
        ) {
            life += -1;
            enemyBulletList.splice(i,1)

            if(life < 0) {GameOver()}
        }
    })
}
const lifeUp = () => {   // 최적화를 위해 bullet 객체 attack()함수 콜백함수로 실행  
    for(let i=0; i<5; i++){
        if(score === 20*i && life === 0 && score !== 0) {
            life += 1;
            // console.log('life: ', life);
        }
    }
}

//gameOver
const GameOver = () => {
    gameOver = true;
    ctx.drawImage(gameOverImage, 0, 0, canvas.width, canvas.height)
    ctx.fillText('<Press R key>', canvas.width/5 , 95)
    ctx.fillText('<Your Score : ' + score + '>', canvas.width/5 , 650)
    ctx.fillStyle = 'orange';
    ctx.font = "50px Arial"
}   

const GameStart = () => {
    if(gameStart == true){
        ctx.drawImage(backgroundImage, 0, 0, canvas.width, canvas.height);
        ctx.drawImage(spaceshipImage, spaceshipX, spaceshipY, 60, 60);
        ctx.fillText('  START', canvas.width/3 , 95)
        ctx.fillText('Press Space key', canvas.width/5 , 150)
        ctx.fillStyle = 'orange';
        ctx.font = "50px Arial"
        requestAnimationFrame(GameStart);
    }
    else{
      main();
    }
} 

function render(){
        ctx.drawImage(backgroundImage, 0, 0, canvas.width, canvas.height);
        ctx.drawImage(spaceshipImage, spaceshipX, spaceshipY, 60, 60);
        for (let i=0; i<bulletList.length; i++){
            if(bulletList[i].alive){
                bulletList[i].attack(lifeUp);
                ctx.drawImage(bulletImage, bulletList[i].x, bulletList[i].y, 10, 10)
            }
        };
        for (let i=0; i<enemyList.length; i++){
            if(enemyList[i].alive){
                ctx.drawImage(enemyImage, enemyList[i].x, enemyList[i].y, 30, 30)
            } else {
                // ctx.drawImage(enemyBoomImage, enemyList[i].x, enemyList[i].y, 30, 30)
            }
        }
        for (let i=0; i<enemyList2.length; i++){
            if(enemyList2[i].alive){
                ctx.drawImage(enemyImage2, enemyList2[i].x, enemyList2[i].y, 40, 40)
            }
        }
        for (let i=0; i<enemyBulletList.length; i++){
            if(enemyBulletList[i].alive) {
                ctx.drawImage(enemyBulletImage, enemyBulletList[i].x, enemyBulletList[i].y, 20, 20)
            }
        }
        for (let i=0; i<enemyList3.length; i++){
            if(enemyList3[i].alive){
                ctx.drawImage(enemyImage3, enemyList3[i].x, enemyList3[i].y, 40, 40)
            } else {
                // ctx.drawImage(enemyBoomImage, enemyList[i].x, enemyList[i].y, 30, 30)
            }
        }
        for (let i=0; i<enemyList4.length; i++){
            if(enemyList4[i].alive){
                ctx.drawImage(enemyImage3, enemyList4[i].x, enemyList4[i].y, 40, 40)
            } else {
                // ctx.drawImage(enemyBoomImage, enemyList[i].x, enemyList[i].y, 30, 30)
            }
        }
        ctx.fillText('score: '+ score, 20, 25);
        ctx.fillStyle = 'white';
        ctx.font = "20px Arial"
        if(life === 1){
            ctx.drawImage(lifeImage, 120, 5, 30, 30)
        }
}

function main(){
    if(!gameOver && score < 50) {
        render() // 랜더 => 그려준다
        spaceshipMove();
        bulletMove();
        enemyBulletMove();
        moveEnemy();
        moveEnemy2();
        moveEnemy3();
        moveEnemy4();
        lifeDown(GameOver); // 에너지 깎일때 마다 GameOver 콜백으로 부르자
        requestAnimationFrame(main); // 애니매이션 호출
    } else if(!gameOver && score >= 50) { // 게임 클리어!
        ctx.drawImage(gameClear, 0, 0, canvas.width, canvas.height)
    } else {
        GameOver();
        //var name = prompt('이름을 입력하세요 : ');
    }
}

loadImage();
keyboardListener();
GameStart();
//main();