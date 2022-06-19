package address.step1;

import java.awt.event.ActionEvent; // 이벤트 핸들러 클래스
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import address.view2.DBConnectionMgr;

public class EmpSumList extends JFrame implements ActionListener {
	JButton jbtn_select = new JButton("조회");
	
	String headers[] = {"부서명", "CLERK", "MANAGER", "ETC", "DEPT", "DEPT_SAL"}; // 컬럼 헤더로 올 데이터
	String data[][] = new String[0][5]; // 앞에는 헤더라서 [0] - 0행 5열
	DefaultTableModel dtm = new DefaultTableModel(data, headers); // 데이터셋(웹에서는 Json과 비슷한 역할)
	JTable jtb = new JTable(dtm); // 위 데이터셋과 바인딩해야함
	JScrollPane jsp = new JScrollPane(jtb); // 위 JTble을 파라미터로 넘김
	
	// 물리적으로 떨어져 있는 오라클 서버에 연결통로 확보
	Connection con = null;
	// 개발자가 작성한 select문 전달하고 오라클 서버에 처리 요청
	PreparedStatement pstmt = null;
	// 오라클 서버에서 조회한 결과를 반환해 주면 커서 조작하기
	ResultSet rs = null;
	DBConnectionMgr dbMgr = new DBConnectionMgr();
	
	
	public EmpSumList() {
		jbtn_select.addActionListener(this);
		initDisplay();
	}
	
	
	public List<Map<String,Object>> getEmpSumList(){
		
		System.out.println("getEmpSumList 호출 성공");
	
		List<Map<String,Object>> list = new ArrayList<>(); // Map데이터를 List에 삽입
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT                                                                ");
		sql.append("decode(b.rno, '1', A.dname, '총계')                                   ");
		sql.append(",sum(clerk)clerk                                                      ");
		sql.append(",sum(manager)manager                                                  ");
		sql.append(",sum(etc)etc                                                          ");
		sql.append(",sum(dept_sal) dept_sal                                               ");
		sql.append("FROM  (                                                               ");
		sql.append(" SELECT dept.dname                                                    ");
		sql.append("       ,sum(decode(JOB, 'CLERK', sal)) clerk                          ");
		sql.append("       ,sum(decode(JOB, 'MANAGER', sal)) manager                      ");
		sql.append("       ,sum(decode(JOB, 'CLERK', NULL,'MANAGER', NULL, sal)) etc      ");
		sql.append("       ,sum(sal) dept_sal                                             ");
		sql.append("  FROM emp, dept                                                      ");
		sql.append("  WHERE emp.deptno = dept.deptno                                      ");
		sql.append("GROUP BY dept.dname                                                   ");
		sql.append(")A,                                                                   ");
		sql.append("(                                                                     ");
		sql.append(" SELECT ROWNUM rno FROM dept                                          ");
		sql.append(" WHERE ROWNUM <3                                                      ");
		sql.append(" )B                                                                   ");
		sql.append(" GROUP BY decode(b.rno, '1', A.dname, '총계')                         ");
		sql.append(" ORDER BY decode(b.rno, '1', A.dname, '총계')                         ");
		
		try {
			con = dbMgr.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			
			Map<String, Object> rmap = null;
			while(rs.next()) {
				rmap = new HashMap<>();
				rmap.put("dname", rs.getString(1));
				rmap.put("clerk", rs.getDouble(2));
				rmap.put("manager", rs.getDouble(3));
				rmap.put("etc", rs.getDouble(4));
				rmap.put("dept_sal", rs.getDouble(5));
				list.add(rmap);
			}
			
			while(dtm.getRowCount() > 0) {
				dtm.removeRow(0);
			}
			
			
			Iterator<Map<String,Object>> iter = list.iterator();
			Object keys[] = null;
			
			while(iter.hasNext()) {
				Map<String,Object> data = iter.next();
				keys = data.keySet().toArray();
				Vector<Object> oneRow = new Vector<>();
				oneRow.add(data.get(keys[4]));
				oneRow.add(data.get(keys[3]));
				oneRow.add(data.get(keys[2]));
				oneRow.add(data.get(keys[1]));
				oneRow.add(data.get(keys[0]));
				dtm.addRow(oneRow);				
			}
			
			//System.out.println(list); // 단위테스트
		} catch (Exception e) {
			System.out.println(e.toString());
		} finally {
			DBConnectionMgr.freeConnection(pstmt, con);
		}
		return list;
	}
	
	
	public void initDisplay() {
		System.out.println("initDisplay 호출 성공");
		
		this.add("North", jbtn_select);
		
		this.add("Center", jsp);
		
		this.setTitle("");
		this.setSize(500, 400);
		this.setVisible(true);	
	}
	
	
	public static void main(String[] args) {
		new EmpSumList();
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// 이벤트가 감지된 컴포넌트의 주소번지를 가져온다
		Object obj = e.getSource();
		if(jbtn_select == obj) { // 주소번지가 같니?
			System.out.println("조회 버튼 누른거야!");
			getEmpSumList();
		}
	}

}
