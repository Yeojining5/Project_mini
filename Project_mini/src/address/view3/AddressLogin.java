package address.view3;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;


public class AddressLogin extends JFrame implements ActionListener  {
	/////////////////////////////////////////////////////
	/* 선언부 */
	/////////////////////////////////////////////////////
	Connection		  con	= null;
	PreparedStatement pstmt = null;
	ResultSet 		  rs	= null;
	DBConnectionMgr   dbMgr = new DBConnectionMgr();
	
	String nickName="";
	String imgPath="src\\images\\address\\";
	JLabel jlb_id = new JLabel("아이디");
	JLabel jlb_bday = new JLabel("생년월일(YYYYMMDD)");

	Font jl_font = new Font("휴먼매직체", Font.BOLD, 13);
	JTextField jtf_id = new JTextField("8");
	JPasswordField jpf_bday = new JPasswordField("19900712");

	JButton jbtn_login = new JButton(
			new ImageIcon(imgPath+"login.png"));
			//new ImageIcon("C:\\Users\\minkh\\Desktop\\practice\\dev_java\\src\\com\\Final\\image\\login.png"));
	JButton jbtn_join = new JButton(
			new ImageIcon(imgPath+"confirm.png"));
			//new ImageIcon("C:\\Users\\minkh\\Desktop\\practice\\dev_java\\src\\com\\Final\\image\\co   nfirm.png"));

	// JPanel에 쓰일 이미지아이콘
	//ImageIcon ig = new ImageIcon("C:\\Users\\minkh\\Desktop\\practice\\dev_java\\src\\com\\Final\\image\\main.png");
	ImageIcon ig = new ImageIcon(imgPath+"main.PNG");
	
	ModifyDialog mDialog = null;
	
	AddressBook abook = null;
	
	/////////////////////////////////////////////////////
	/* 생성자 */
	/////////////////////////////////////////////////////
	
	public AddressLogin(){
		initDisplay();
	}

	/////////////////////////////////////////////////////
	/* jpanal 오버라이드 */
	/////////////////////////////////////////////////////

	/* 배경이미지 */
	class mypanal extends JPanel {
		public void paintComponent(Graphics g) {
			g.drawImage(ig.getImage(), 0, 0, null);
			setOpaque(false);
			super.paintComponents(g);
		}
	}

	
	///////////////////////////////////////////////// 화면 그리기
	public void initDisplay () {
		setContentPane(new mypanal());
		
		/* 버튼과 텍스트필드 구성 */
		jbtn_join.addActionListener(this);
		jbtn_login.addActionListener(this);
		this.setLayout(null);
		this.setTitle("주소록 데모 프로그램 로그인 ver.1");
		this.setSize(350, 600);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setLocation(800, 250);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		// id 라인
		jlb_id.setBounds(45, 200, 80, 40);
		jlb_id.setFont(jl_font);
		jtf_id.setBounds(200, 200, 100, 40);
		this.add(jlb_id);
		this.add(jtf_id);

		// birthday 라인 (비밀번호 역할)
		jlb_bday.setBounds(45, 240, 130, 40);
		jlb_bday.setFont(jl_font);
		jpf_bday.setBounds(200, 240, 100, 40);
		this.add(jlb_bday);
		this.add(jpf_bday);

		// 로그인 버튼 라인
		jbtn_login.setBounds(175, 285, 120, 40);
		this.add(jbtn_login);

		// 회원가입 버튼 라인
		jbtn_join.setBounds(45, 285, 120, 40);
		this.add(jbtn_join);		
		
		
	}
	
	/*******************************************************************
	 * 로그인 구현
	 * @param id - 사용자가 입력한 아이디 받아오기
	 * @param birthday - 사용자가 입력한 생년월일 받아오기(비밀번호 역할)
	 * @return name - 사용자 이름 
	 *******************************************************************/
	
	public String login(int id, String birthday) {
		String name = null;
		
		 // 인라인뷰를 통해 rownum 이 1인 데이터만 다시 추출
//		아이디와 비번이 모두 일치하면  name 조회
//		비번이 틀리면 0 반환
//		아이디가 존재하지 않으면 -1 반환
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT                                      ");
		sql.append("      name                              ");
		sql.append("FROM(                                       ");
		sql.append("   SELECT                                   ");
		sql.append("	 CASE WHEN id=? THEN                ");
		sql.append("	   CASE WHEN birthday=? THEN name     ");
        sql.append("      	 ELSE '0'                           ");
        sql.append("      	 END                                ");
        sql.append("      ELSE '-1'                             ");
        sql.append("      END AS name                       ");
        sql.append("     FROM mkaddrtb                            ");
        sql.append("     ORDER BY name desc                 "); 
        sql.append("    )                                       ");
        sql.append("WHERE ROWNUM = 1                            ");
		try {
			con = dbMgr.getConnection();
	    	pstmt = con.prepareStatement(sql.toString()); // 물음표 채우기
	    	pstmt.setInt(1, id);
	    	pstmt.setString(2, birthday);
	    	rs = pstmt.executeQuery();
	    	
	    	// 조회 결과는 0이거나 1을 출력하는 row이므로 if문 사용
	    	if(rs.next()) {
	    		name = rs.getString("name");
	    	}
	    	System.out.println("name : "+name);
		} catch (SQLException se) {
	    	System.out.println("[[query]]"+ sql.toString());
	    	System.out.println(se.toString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 사용한 자원 반납은 생성된 역순으로 한다.
			DBConnectionMgr.freeConnection(rs, pstmt, con);
		}
		return name;
	}
	
	public static void main(String[] args) {
		new AddressLogin();
	}

	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		if (obj == jbtn_join) { ////////// 회원가입 버튼 클릭 시 ModifyDialog 호출
			//ModifyDialog mDialog = new ModifyDialog();
			System.out.println("회원가입하기");
			abook = new AddressBook(); 
			mDialog = new ModifyDialog();
			mDialog.initComponents();
			mDialog.set("회원가입", true, null,abook);
			mDialog.setVisible(true);
		} 
		else if (obj == jbtn_login) { //////////// 로그인 버튼 클릭 + 성공 시 AddressBook 호출 
			int id = Integer.parseInt(jtf_id.getText());
			String birthday = jpf_bday.getText();
			String name = null;
			
			name = login(id, birthday); // login 메소드에서 반환된 name을 name에 담기
			System.out.println("로그인 요청 결과는? " + name);
			
			if(name == null) {
				JOptionPane.showMessageDialog(this, "회원가입 여부를 확인하세요");
				return;
			}
			else if(name.equals("0")) {
				JOptionPane.showMessageDialog(this, "비밀번호를 확인하세요.");
				return;
			}
			else if(name.equals("-1")) {
				JOptionPane.showMessageDialog(this, "아이디가 존재하지 않습니다.");
				return;
			}
			else {
				this.dispose(); // 이름이 있다면 로그인창을 닫아라
				abook = new AddressBook(); 
		        abook.initComponents();
		        abook.setVisible(true);
			}
		}
	}
}