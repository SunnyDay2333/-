import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.*;


public class BlocksGameView extends JFrame implements KeyListener {
    protected static final int game_x=28;
    protected static final int game_y=16;//游戏界面的行、列数
    public JTextArea text[][];//设置文本域
    int[][] data;//数据数组 1代表有方块
    static JButton btn[] = new JButton[2];//按钮
    static String[] btnLabel= new String[2];//按钮标签
    int score = 0;//得分
    JTextField tex1 = new JTextField(String.valueOf(score),20);//游戏得分
    JTextField tex2 = new JTextField(20);//游戏状态
    

    //初始化窗体的方法
    public void initWindow(){
        this.setSize(590,695);//宽590 高695的窗口
        this.setVisible(true);//窗口可见
        this.setLocationRelativeTo(null);//窗口居中
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//释放窗体
        this.setResizable(false);//窗口不可变
        this.setTitle("俄罗斯方块 Java课程设计——XuLiang HNU");
    }

    //初始化游戏界面的方法
    public void initGamePanel(){
        JPanel game_main = new JPanel();
        game_main.setLayout(new GridLayout(game_x,game_y,1,1));
        for(int i = 0 ; i<text.length; i++){
            for(int j =0 ; j<text[i].length;j++){
                //设置文本域行列
                text[i][j] = new JTextArea(game_x,game_y);
                //设置背景颜色
                text[i][j].setBackground(Color.WHITE);
                //添加键盘监听事件
                text[i][j].addKeyListener(this);
                //初始化游戏边界
                if(j==0 || j==text[i].length-1 || i==text.length-1){
                    text[i][j].setBackground(Color.BLACK);
                    data[i][j]=1;
                }
                //设置文本域不可编辑
                text[i][j].setEditable(false);
                //文本域添加到主面板
                game_main.add(text[i][j]);
            }
        }
        
        this.setLayout(new BorderLayout());
        this.add(game_main,BorderLayout.CENTER);
    }

    //给游戏添加焦点，解决点击按钮导致无法监听游戏的问题
    public void setFocus(){
        for(int i = 0 ; i<text.length; i++){
            for(int j =0 ; j<text[i].length;j++){
                text[i][j].requestFocus();
            }
        }
    }

    //添加窗口组件
    public void addComponent(){
        JPanel menus = new JPanel();//新建菜单页面，在主窗口的右边包含文本框 按钮 说明等板块
        menus.setLayout(new GridLayout(4,1,1,10));//网格型 4行 一列

        //以下为按钮界面
        JPanel buttonsPanel = new JPanel();
        btnLabel = new String[]{"开始游戏","暂停"};
        buttonsPanel.setLayout(new GridLayout(4,1,1,10));
        for(int i=0;i<2;i++){
            btn[i] = new JButton(btnLabel[i]);
            buttonsPanel.add(btn[i]);
        }
        //以下为文本框界面
        JPanel textField = new JPanel();
        textField.setLayout(new GridLayout(4,2,1,10));
        JLabel la1 = new JLabel("得分：");
        JLabel la2 = new JLabel("游戏状态：");
        textField.add(la1);
        textField.add(tex1);
        textField.add(la2);
        textField.add(tex2);
        //以下为说明
        JTextArea introduces = new JTextArea("按下开始游戏即可游玩\n方块超过上方第四行结束游戏\n旋转：W 或 ↑\n左移：A 或 ←\n右移：D 或 →\n速降：S 或 ↓  \n暂停/恢复：P\n每消除一行速度都会加快哦",9,10);
        introduces.setBackground(null);
        introduces.setEditable(false);
        introduces.setFont(new Font("微软雅黑", Font.BOLD, 14));
 

        menus.add(textField);
        menus.add(buttonsPanel);
        menus.add(introduces);
        this.add(menus,BorderLayout.EAST);

    }

    public BlocksGameView(){
        text = new JTextArea[game_x][game_y];
        data = new int[game_x][game_y];
        initGamePanel();
        addComponent();
        initWindow();
    }


    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        
    }

    @Override
    public void keyReleased(KeyEvent e) {
       
    }


    

}