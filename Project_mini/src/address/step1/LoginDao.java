package address.step1;
// JDBC API 공통코드 클래스를 따로 설계함 = 외부 클래스에서 인스턴스화 하여 호출 사용
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import address.view2.DBConnectionMgr;

public class LoginDao extends JFrame implements ActionListener {
	JPanel      jp_north    = new JPanel();
	JTextField  jtf_id      = new JTextField("",10);
	JButton     jbtn_check  = new JButton("ID중복검사");
	JButton     jbtn_join   = new JButton("회원가입");
	boolean     isIDCheck   = false;
	
	public LoginDao() {
		jbtn_check.addActionListener(this);
	   // initDisplay();  // 생성자에서 화면을 그리는 메소드 호출
	}
	
	public void initDisplay() {
		jp_north.setLayout(new BorderLayout());
		jp_north.add("Center", jtf_id);
		jp_north.add("East", jbtn_check);
		this.add("North", jp_north);
		jbtn_join.setEnabled(false);
		this.add("South", jbtn_join);
		this.setSize(500, 300);;
		this.setVisible(true);
	}
	
	/***************************************************************
	 * 로그인 구현
	 * @param mem_id  -  사용자가 입력한 아이디 받아오기
	 * @param mem_pw  -  사용자가 입력한 비번 받아오기
	 * @return
	 */
	
	
	public String login(String mem_id, String mem_pw) {
		String mem_name = null;
					// 물리적으로 떨어져있는 오라클 서버와 연결통로 만들기
					Connection con = null;
					// 오라클 서버에 작성한 select문 전달하고 오라클 서버에 처리 요청할 때 사용
					PreparedStatement pstmt = null;
					// 조회 결과를 자바코드로 가져올 때 필요 - 오라클 서버의 커서를 조작하는데 필요함.
					ResultSet rs = null;
					String result = "";
					StringBuilder sql = new StringBuilder();
					sql.append("SELECT result                                 ");
				    sql.append(" FROM  (                                      ");
				    sql.append("       SELECT                                 ");
				    sql.append("          CASE WHEN mem_id=? THEN             ");
				    sql.append("              CASE WHEN mem_pw=? THEN mem_name");
				    sql.append("                ELSE '0'                       ");
				    sql.append("              END                             ");
				    sql.append("          ELSE '-1'                            ");
				    sql.append("         END AS result                        ");
				    sql.append("   FROM member                                ");
				    sql.append("  ORDER BY result desc                        ");
				    sql.append("          )                                   ");
				    sql.append("  WHERE ROWNUM = 1                            ");
				    
				    DBConnectionMgr dbMgr = new DBConnectionMgr();
				    
				    try {
				    	con = dbMgr.getConnection();
				    	// ? 자리에 들어갈 아이디를 설정해야 함
						pstmt = con.prepareStatement(sql.toString());
						pstmt.setString(1, mem_id);
						pstmt.setString(2, mem_pw);
						// select 처리 시는 executeQuery() 호출
						// insert, update, delete 처리 시는 executeUpdate() 호출
						rs = pstmt.executeQuery();
						if(rs.next()) {  // 조회결과가 참이니
							result = rs.getString("result");
						}
						System.out.println("result : "+ result);
					} catch (Exception e) {
						System.out.println(e.toString());
					} 
				  return mem_name;
			}
		
	/**********************************************************
	 * 아이디 중복체크 
	 * @param mem_id - 사용자가 입력한 아이디
	 * @return 1 : 아이디 존재함, 0: 아이디 사용가능함, -1: 디폴트(end of file, 찾을수없다)
	 **********************************************************/
	
	public int idCheck(String mem_id) {
			// 물리적으로 떨어져있는 오라클 서버와 연결통로 만들기
			Connection con = null;
			// 오라클 서버에 작성한 select문 전달하고 오라클 서버에 처리 요청할 때 사용
			PreparedStatement pstmt = null;
			// 조회 결과를 자바코드로 가져올 때 필요 - 오라클 서버의 커서를 조작하는데 필요함.
			ResultSet rs = null;
			int result = -1;
			StringBuilder sql = new StringBuilder();
			sql.append("SELECT nvl((                                ");
		    sql.append("        SELECT 1                            ");
		    sql.append("          FROM dual                         ");
		    sql.append("           WHERE EXISTS (SELECT MEM_NAME    ");
		    sql.append("                           FROM MEMBER      ");
		    sql.append("                          WHERE MEM_ID=?)   ");
		    sql.append("  ),0)                                      ");
		    sql.append("  FROM dual                                 ");
		    
		    DBConnectionMgr dbMgr = new DBConnectionMgr();
		    
		    try {
		    	con = dbMgr.getConnection();
		    	// ? 자리에 들어갈 아이디를 설정해야 함
				pstmt = con.prepareStatement(sql.toString());
				pstmt.setString(1, mem_id);
				// select 처리 시는 executeQuery() 호출
				// insert, update, delete 처리 시는 executeUpdate() 호출
				rs = pstmt.executeQuery();
				if(rs.next()) {  // 조회결과가 참이니
					result = rs.getInt(1);
				}
				System.out.println("result : "+ result);
			} catch (Exception e) {
				System.out.println(e.toString());
			} 
		    return result;
	}
	public static void main(String[] args) {
		LoginDao loginDao = new LoginDao();
		loginDao.initDisplay();
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		Object obj = e.getSource();
		// ID중복체크 버튼 눌린거니?
		if(obj == jbtn_check) {
			System.out.println("ID중복체크 호출 성공");
			// 사용자가 입력한 아이디를 가져와 담기
			String user_id = jtf_id.getText();
			int result = idCheck(user_id);
			// 입력한 아이디가 존재한다 1 / 존재하지x 0
			if(result == 1) {
				// 입력한 아이디는 사용 못해. 다시 입력해야 된다.
				jtf_id.setText("");
				JOptionPane.showMessageDialog(this,  "입력한 아이디는 사용할 수 없습니다.", "Error", JOptionPane.ERROR_MESSAGE);
				return;  // if문 안에서 return을 만나면 함수를 빠져나감. 여기서는 actionPerformed에서 빠져나감
			} else if (result == 0) {
				JOptionPane.showMessageDialog(this,  "사용 가능한 아이디입니다.", "INFO", JOptionPane.INFORMATION_MESSAGE);
				jtf_id.setEditable(false);
				jbtn_check.setEnabled(false);
				isIDCheck = true;
				jbtn_join.setEnabled(isIDCheck);
			}
				
	     }
	
      }
   }
