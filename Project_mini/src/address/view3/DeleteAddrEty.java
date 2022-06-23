package address.view3;

import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.swing.JOptionPane;


public class DeleteAddrEty {
	
	DBConnectionMgr 	dbMgr	= new DBConnectionMgr();
	Connection 			con		= null;
	PreparedStatement 	ps 		= null;
    /****************************************************************************
     * 회원삭제 구현
	   DELETE FROM mkaddrtb
	    WHERE id = ?
     * @param vo - 사용자가 입력했던 값 담기
     * @return - 삭제 성공 여부 담기
     ***************************************************************************/

	public AddressVO delete(AddressVO vo) {
		System.out.println("DeleteAddrEty delete 호출 성공");
		
		StringBuilder sql = new StringBuilder();
		sql.append(" DELETE FROM mkaddrtb WHERE id = ?  ");
		
		try {
			con = dbMgr.getConnection();
			ps = con.prepareStatement(sql.toString());
			
			ps.setInt(1, vo.getId()); // WHERE id = ? 채우기
			
			if(ps.executeUpdate() < 1) {
				String msg = "데이터 입력에 실패했습니다.";
				System.out.println(msg);
			} else {
				System.out.println("데이터 입력에 성공했습니다.");
				vo.setResult(1);
			}			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnectionMgr.freeConnection(ps, con);
		}
		return vo;
	}

}
