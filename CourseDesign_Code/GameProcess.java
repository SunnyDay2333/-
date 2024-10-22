import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;
import javax.swing.*;
public class GameProcess extends BlocksGameView implements KeyListener{
    static int startSymbol;//游戏状态标识---1代表开始
    private int block;//当前方块（二进制数组）
    private int x,y;//当前方块的坐标
    protected int sleepTime = 1000;//线程休眠时间
    boolean gamePause= false;//是否暂停标识符
    static int pauseClickCount = 0;
    private int[] allBlocks = new int[22];
    //游戏开始方法
    public void gameBegin(){
        while(true){
            if(startSymbol==0){
                break;
            }
            gameRun();
        }
        tex2.setText("游戏结束！");
        JOptionPane.showMessageDialog(null, "GameOver");//弹出提示框
    }
    //生成随机方块方法
    public void ranBlocks(){
        Random random = new Random();
        block=allBlocks[random.nextInt(22)];
    }
    //游戏运行方法
    public void gameRun(){
        ranBlocks();//生成随机方块
        setFocus();
        x=0;
        y=6;
        for(int i=0;i<game_x;i++){
            try {
                Thread.sleep(sleepTime);
                if(gamePause){
                    i--;
                }else{
                if(!canFall(x,y)){
                    changeData(x,y);//data置为1
                    //判断是否可以消除
                    for(int j =x; j<x+4;j++){
                        int sum=0;
                        for(int k =1 ; k<=(game_y-2);k++){
                            if(data[j][k]==1){
                                sum++;
                            }
                        }
                        if(sum==(game_y-2)){
                            clearRow(j);
                        }
                    }
                    //判断游戏是否失败
                    for(int j =1; j<=(game_y-2);j++){
                        if(data[3][j]==1){
                            startSymbol=0;
                            break;
                        }
                    }
                    break;
                }else{
                    x++;
                    fall(x,y);
                }
              } 
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    //判断能不能下降
    public boolean canFall(int m,int n){
        int temp = 0x8000;
        for(int i = 0;i<4;i++){
            for(int j = 0;j<4;j++){
                if((temp&block)!=0){
                    if(data[m+1][n]==1){
                        return false;
                    }
                }
                n++;
                temp>>=1;
            }
            m++;
            n=n-4;
        }
        return true;
    }

    //改变不可下降方块对应的值的方法
    public void changeData(int m,int n){
        int temp = 0x8000;
        for(int i=0;i<4;i++){
            for(int j=0;j<4;j++){
                if((temp&block)!=0){
                    data[m][n] = 1;
                }
                n++;
                temp>>=1;
            }
            m++;
            n=n-4;
        }
    }
    public void clearRow(int row){
        int temp =200;
        for(int i = row; i>=1; i--){
            for(int j=1;j<=(game_y-2);j++){
                data[i][j]=data[i-1][j];
            }
        }
        reflash(row);

        if(sleepTime > temp){
            sleepTime -= 100;
        }//每消除一行提高下落速度

        score += 10;
        tex1.setText(String.valueOf(score));
    }

    //刷新移除某一行后游戏界面的方法
     public void reflash(int row){
        for(int i =row;i>=1;i--){
            for(int j=1;j<=(game_y-2);j++){
                if(data[i][j]==1){
                    text[i][j].setBackground(Color.ORANGE);
                }else{
                    text[i][j].setBackground(Color.WHITE);
                }
            }
        }
     }

    //方块下落方法
    public void fall(int m,int n){
        if(m>0){
            //清除上一层的方块颜色
            clearColor(m-1,n);
        }
        reBuild(m,n);//重新绘制方块
    }

    //方块下落后清除上一层的颜色的方法
    public void clearColor(int m,int n){
        int temp = 0x8000;
        for(int i = 0;i<4;i++){
            for(int j=0;j<4;j++){
                if((temp&block)!=0){
                    text[m][n].setBackground(Color.WHITE);
                }
                n++;
                temp>>=1;
            }
            m++;
            n=n-4;
        }
    }

    //重新绘制方块的方法
    public void reBuild(int m,int n){
        int temp = 0x8000;
        for(int i=0;i<4;i++){
            for(int j=0;j<4;j++){
                if((temp&block)!=0){
                    text[m][n].setBackground(Color.ORANGE);
                }
                n++;
                temp>>=1;
            }
            m++;
            n=n-4;
        }
    }

     //判断方块此时是否可以变形的方法
     public boolean canTurn(int a, int m, int n) {
        //创建变量
        int temp = 0x8000;
        //遍历整个方块
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if ((a & temp) != 0) {
                    if (data[m][n] == 1) {
                        return false;
                    }
                }
                n++;
                temp >>= 1;
            }
            m++;
            n = n - 4;
        }
        //可以变形
        return true;
    }

    public GameProcess(){
        //初始化方块
        allBlocks = new int[]{0x00cc, 0x8888, 0x000f, 0x888f, 0xf888, 0xf111,
            0x111f, 0x0eee, 0xffff, 0x0008, 0x0888, 0x000e, 0x0088,
            0x000c, 0x08c8, 0x00e4, 0x04c4, 0x004e, 0x08c4,
            0x006c, 0x04c8, 0x00c6};
        //给按钮添加监听器
        //btn[0]控制开始游戏
        btn[0].addActionListener(new ActionListener(){
           public void actionPerformed(ActionEvent e) {
              startSymbol=1;
              btn[0].setEnabled(false);//开始后不可触发
              tex2.setText("游戏中...");
              tex2.setForeground(Color.BLUE);
              tex2.setFont(new Font("微软雅黑", Font.BOLD, 14));
              /*gameBegin() 方法执行的任务量太大，可能会阻塞界面的事件分发线程（Event Dispatch Thread）从而导致卡死。
                所以单独分配一个线程**/
              Thread gameThread = new Thread(new Runnable() {
                public void run() {
                    gameBegin();
                }
            });
            // 启动线程
            gameThread.start();
          }
            
         });
        //btn[1]控制暂停
        btn[1].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                if(startSymbol==0){
                    return;
                }
                pauseClickCount++;
                //按下一次，暂停
                if(pauseClickCount==1){
                    gamePause=true;
                    tex2.setText("已暂停");
                    tex2.setForeground(Color.PINK);
                    tex2.setFont(new Font("微软雅黑", Font.BOLD, 14));
                }
                //按下两次，恢复暂停
                if(pauseClickCount==2){
                    gamePause=false;
                    pauseClickCount=0;//重新开始计数
                    tex2.setText("游戏中...");
                    tex2.setForeground(Color.BLUE);
                    tex2.setFont(new Font("微软雅黑", Font.BOLD, 14));
                    setFocus();
                }
            }
        });
    }
    
    public static void main(String[] args) {
        GameProcess gameExec = new GameProcess();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        //方块进行左移
    if (KeyEvent.VK_LEFT == e.getKeyCode() || KeyEvent.VK_A == e.getKeyCode()) {
        //判断游戏是否结束
        if (startSymbol==0) {
            return;
        }
    
        //判断游戏是否暂停
        if(gamePause){return;}
    
        //方块是否碰到左墙壁
        if (y <= 1) {
            return;
        }
    
        //定义一个变量
        int temp = 0x8000;
    
        for (int i = x; i < x + 4; i++) {
            for (int j = y; j < y + 4; j++) {
                if ((temp & block) != 0) {
                    if (data[i][j - 1] == 1) {
                        return;
                    }
                }
                temp >>= 1;
            }
        }
    
        //首先清除目前方块
        clearColor(x, y);
        y--;
        reBuild(x, y);
    }
    
       //方块进行右移
       if (KeyEvent.VK_RIGHT == e.getKeyCode() || KeyEvent.VK_D == e.getKeyCode()) {
          //判断游戏是否结束
          if (startSymbol==0) {
            return;
          }
    
        //判断游戏是否暂停
        if(gamePause){return;}
    
        //定义变量
        int temp = 0x8000;
        int m = x;
        int n = y;
    
        //存储最右边的坐标值
        int num = 1;
    
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if ((temp & block) != 0) {
                    if (n > num) {
                        num = n;
                    }
                }
                n++;
                temp >>= 1;
            }
            m++;
            n = n - 4;
        }
    
        //判断是否碰到右墙壁
        if (num >= (game_y - 2)) {
            return;
        }
    
        //方块右移途中是否碰到别的方块
        temp = 0x8000;
        for (int i = x; i < x + 4; i++) {
            for (int j = y; j < y + 4; j++) {
                if ((temp & block) != 0) {
                    if (data[i][j + 1] == 1) {
                        return;
                    }
                }
                temp >>= 1;
            }
        }
    
        //清除当前方块
        clearColor(x, y);
        y++;
        reBuild(x, y);
    }
    
    //方块进行下落
    if (KeyEvent.VK_DOWN == e.getKeyCode() || KeyEvent.VK_S == e.getKeyCode()) {
        //判断游戏是否结束
        if (startSymbol==0) {
            return;
        }
    
        //判断游戏是否暂停
        if(gamePause){return;}
    
        //判断方块是否可以下落
        if (!canFall(x, y)) {
            return;
        }
    
        clearColor(x, y);
    
        //改变方块的坐标
        x++;
    
        reBuild(x, y);
    }
    //键盘控制游戏暂停
    if(KeyEvent.VK_P == e.getKeyCode()){
        if(startSymbol==0){
            return;
        }
        pauseClickCount++;
        //按下一次，暂停
        if(pauseClickCount==1){
            gamePause=true;
            tex2.setText("已暂停");
            tex2.setForeground(Color.PINK);
            tex2.setFont(new Font("微软雅黑", Font.BOLD, 14));
        }
        //按下两次，恢复暂停
        if(pauseClickCount==2){
            gamePause=false;
            pauseClickCount=0;//重新开始计数
            tex2.setText("游戏中...");
            tex2.setForeground(Color.RED);
            tex2.setFont(new Font("微软雅黑", Font.BOLD, 14));
        }
    
    }
    
    
    
    //控制方块进行变形
    if (KeyEvent.VK_UP == e.getKeyCode() || KeyEvent.VK_W == e.getKeyCode()) {
        //判断游戏是否结束
        if (startSymbol==0) {
            return;
        }
    
        //判断游戏是否暂停
        if(gamePause){return;}
        
    
        //定义变量,存储目前方块的索引
        int old;
        for (old = 0; old < allBlocks.length; old++) {
            //判断是否是当前方块
            if (block == allBlocks[old]) {
                break;
            }
        }
    
        //定义变量,存储变形后方块
        int next;
    
        //判断是方块
        if (old == 0 || old == 7 || old == 8 || old == 9) {
            return;
        }
    
        //清除当前方块
        clearColor(x, y);
    
        if (old == 1 || old == 2) {
            next = allBlocks[old == 1 ? 2 : 1];
    
            if (canTurn(next, x, y)) {
                block = next;
            }
        }
    
        if (old >= 3 && old <= 6) {
            next = allBlocks[old + 1 > 6 ? 3 : old + 1];
    
            if (canTurn(next, x, y)) {
                block = next;
            }
        }
    
        if (old == 10 || old == 11) {
            next = allBlocks[old == 10 ? 11 : 10];
    
            if (canTurn(next, x, y)) {
                block = next;
            }
        }
    
        if (old == 12 || old == 13) {
            next = allBlocks[old == 12 ? 13 : 12];
    
            if (canTurn(next, x, y)) {
                block = next;
            }
        }
    
        if (old >= 14 && old <= 17) {
            next = allBlocks[old + 1 > 17 ? 14 : old + 1];
    
            if (canTurn(next, x, y)) {
                block = next;
            }
        }
    
        if (old == 18 || old == 19) {
            next = allBlocks[old == 18 ? 19 : 18];
    
            if (canTurn(next, x, y)) {
                block = next;
            }
        }
    
        if (old == 20 || old == 21) {
            next = allBlocks[old == 20 ? 21 : 20];
    
            if (canTurn(next, x, y)) {
                block = next;
            }
        }
        //重新绘制变形后方块
        reBuild(x, y);
    
     }
    
     }

      
    
}