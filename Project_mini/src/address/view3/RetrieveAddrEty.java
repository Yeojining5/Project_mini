package address.view3;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;


public class RetrieveAddrEty {
	
	DBConnectionMgr 	dbMgr 	= new DBConnectionMgr();
	Connection 			con 	= null;// 연결통로
	PreparedStatement 	pstmt 	= null;// DML구문 전달하고 오라클에게 요청
	ResultSet 			rs		= null;// 조회경우 커서를 조작 필요
	
	/***************************************************************************
	 * 회원정보 중 상세보기 구현 - 1건 조회
	 * SELECT name, address, telephone, DECODE(gender,'1','남','여')
             , relationship, birthday, comments, registedate, id 
         FROM mkaddrtb
        WHERE id = ?
	 * @param vo : 사용자가 선택한 값
	 * @return AddressVO : 조회 결과 1건을 담음
	 **************************************************************************/
	public AddressVO retrieve(AddressVO vo) {
		System.out.println("RetrieveAddrEty retrieve(AddressVO vo) 호출 성공");
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT name, address, telephone, DECODE(gender,'1','남','여') ");
		sql.append("        , relationship, birthday, comments, registedate, id   ");
		sql.append("  FROM mkaddrtb                                               ");
		sql.append(" WHERE id = ?                                                 "); 
		
		AddressVO rVO = null;
		int id = vo.getId();
		
		try {
			con = dbMgr.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			pstmt.setInt(1, id);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				rVO = new AddressVO();
				rVO.setName(rs.getString("name"));
				rVO.setAddress(rs.getString("address"));
				rVO.setTelephone(rs.getString("telephone"));
				rVO.setGender(rs.getString("gender"));
				rVO.setRelationship(rs.getString("relationship"));
				rVO.setBirthday(rs.getString("birthday"));
				rVO.setComments(rs.getString("comments"));
				rVO.setRegistedate(rs.getString("registedate"));
				rVO.setId(rs.getInt("id"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnectionMgr.freeConnection(rs, pstmt, con);
		}
		
		return rVO;
	}
	
	
	
	/***************************************************************************
	 * 회원 목록 보기 구현 - n건 조회
	 * SELECT name, address, telephone, DECODE(gender,'1','남','여')
              , relationship, birthday, comments, registedate, id 
         FROM mkaddrtb
	 * @return AddressVO[] : 조회 결과 n건을 담음
	 **************************************************************************/
	public AddressVO[] retrieve() {
		System.out.println("RetrieveAddrEty retrieve() 호출 성공");		
		AddressVO[] vos = null;
		Vector v = new Vector(1,1);
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT name, address, telephone, DECODE(gender,'1','남','여')  ");
		sql.append("        , relationship, birthday, comments, registedate, id    ");
		sql.append("  FROM mkaddrtb                                                ");
		sql.append("  ORDER BY id desc                                             ");
		
		try {
			con = dbMgr.getConnection();
			pstmt = con.prepareStatement(sql.toString());
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				//vos = new AddressVO[8];
				AddressVO vo = new AddressVO(rs.getString(1), rs.getString(2), rs.getString(3)
							      , rs.getString(4), rs.getString(5), rs.getString(6)
								  , rs.getString(7), rs.getString(8), rs.getInt(9));
				v.addElement(vo);
			}
			vos = new AddressVO[v.size()];
			v.copyInto(vos);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnectionMgr.freeConnection(rs, pstmt, con);
		}
		return vos;
	}////////////////////////////////////// end of public AddressVO[] retrieve()
	
	
}

/*
ID,NAME,ADDRESS,TELEPHONE,GENDER,RELATIONSHIP,BIRTHDAY,COMMENTS,REGISTEDATE
3,나초보,서울시 마포구 공덕동,025556968,2,2,19801215,주연테크,REGISTEDATE
1,홍길동,서울시 영등포구 당산동,111,1,동창,19901010,222,20081117
2,이순신,서울시 서초구 양재동,222,2,회사동료,19901110,333,20100113
4,강감찬,경기도 광명시,11,1,동창,19801120,테스트합니다.,2011-03-20
9,나초조,4455,44455,1,44555,19800702,자영업,20110320
7,223,223,223,2,223,223,223,20190609
10,조조,경기도 화성시,44455,1,44555,19800702,자영업,20110320
*/