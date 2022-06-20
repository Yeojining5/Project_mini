package address.view3;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ModifyAddrEty {
	
	DBConnectionMgr 	dbMgr	= new DBConnectionMgr();
	Connection 			con		= null;
	PreparedStatement 	ps 		= null;
    /****************************************************************************
     * 회원수정 구현
		UPDATE MKADDRTB
		   SET NAME = ?, address = ?, telephone = ?
		      ,gender = ?, relationship = ?, birthday = ?
		      ,comments = ?, registedate = ?
		WHERE ID = ?
     * @param vo - 사용자가 입력한 값을 받는다
     * @return - 등록 성공 여부 담기
     ***************************************************************************/
	
	public AddressVO modify(AddressVO vo) {
		System.out.println("ModifyAddrEty modify 호출 성공");
		
		StringBuilder sql = new StringBuilder();
		sql.append("	UPDATE MKADDRTB                                   ");
		sql.append("	   SET NAME = ?, address = ?, telephone = ?       ");
		sql.append("	      ,gender = ?, relationship = ?, birthday = ? ");
		sql.append("	      ,comments = ?, registedate = ?              ");
		sql.append("	WHERE ID = ?                                      ");
		
		try {
			con = dbMgr.getConnection();
			ps = con.prepareStatement(sql.toString());
			int i = 0;
			ps.setString(++i, vo.getName());
			ps.setString(++i, vo.getAddress());
			ps.setString(++i, vo.getTelephone());
			ps.setString(++i, vo.getGender());
			ps.setString(++i, vo.getRelationship());
			ps.setString(++i, vo.getBirthday());
			ps.setString(++i, vo.getComments());
			ps.setString(++i, vo.getRegistedate());
			ps.setInt(++i, vo.getId());
			
			if(ps.executeUpdate() < 1) {
				String msg = "데이터 입력에 실패했습니다.";
				System.out.println(msg);
			} else {
				System.out.println("데이터 입력에 성공했습니다.");
				vo.setResult(1);
			}
		} catch(SQLException e) {
			String msg = "데이터 입력에 실패했습니다.";
			System.out.println(msg + "\r\n" + e);
		} finally {
				DBConnectionMgr.freeConnection(ps, con);
		} 
		return vo;
	}

}
