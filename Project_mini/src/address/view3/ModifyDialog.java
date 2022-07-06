package address.view3;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;


class ModifyDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	// 입력, 수정, 조회 화면에 사용할 컴포넌트를 선언합니다.
	private JLabel labelName;
	private JTextField txtName;
	private JLabel labelAddr;
	private JTextField txtAddress;
	private JLabel labelTel;
	private JTextField txtTel;
	private JLabel labelRel;
	private JTextField txtRelationShip;
	private JLabel labelGender;
	private JComboBox comboGender;
	private JLabel labelBirth;
	private JTextField txtBirthDay;
	private JLabel labelComment;
	private JTextArea txtComment;
	private JLabel labelRegDate;
	private JTextField txtRegDate;
	private JScrollPane scrollPane;
	private JScrollPane scrollComment;
	private JPanel panel;
	private JPanel panelBtn;
	private Font font;

	private Frame parent;
	private String title;

	private JButton btnOk;
	private JButton btnCancel;

	public AddressVO avo = null;
	AddressBook abook = null;
	AddressLogin aLogin = null; 
	private boolean isCancel;

	
	// 생성자는 컴포넌트들을 초기화하는 작업만 합니다.
	public ModifyDialog(){
	}
	
//	public ModifyDialog(AddressBook abook) {
//		this.abook = abook;
//	}
	
	public ModifyDialog(AddressLogin aLogin) {
		this.aLogin = aLogin;
	}
	
	public ModifyDialog(JFrame frm) {
		super(frm,true);
		parent = frm;
		isCancel = true;
		initComponents();
	}

	// 컴포넌트들을 설정하고 배치합니다.
	public void initComponents() { //////////////////////////////
		//다이얼로그에 색상 정의할 때 사용
		this.getContentPane().setBackground(Color.red);
		// 데이터 칼럼명을 보여줄 레이블을 정의합니다.
		labelName = new JLabel("이름(필수입력) ");
		labelAddr = new JLabel("주소 ");
		labelTel = new JLabel("전화번호 ");
		labelRel = new JLabel("관계 ");
		labelGender = new JLabel("성별 ");
		labelBirth = new JLabel("생일(YYYYMMDD) ");
		labelComment = new JLabel("비고 ");
		labelRegDate = new JLabel("수정일 ");

		labelName.setFont(font);
		labelAddr.setFont(font);
		labelTel.setFont(font);
		labelRel.setFont(font);
		labelGender.setFont(font);
		labelBirth.setFont(font);
		labelComment.setFont(font);
		labelRegDate.setFont(font);

		// 데이터를 보여줄 텍스트 필드등을 정의합니다.
		txtName = new JTextField(20);
		txtAddress = new JTextField(20);
		txtTel = new JTextField(20);
		txtRelationShip = new JTextField(20);
		txtBirthDay = new JTextField(20);
		txtComment = new JTextArea(3, 20);
		scrollComment = new JScrollPane(txtComment);
		txtRegDate = new JTextField(20);

		String [] genderList= {"남자", "여자"};
		comboGender = new JComboBox(genderList);

		/////////////////// 확인버튼
		btnOk= new JButton("확인");
		btnOk.setFont(font);
		btnOk.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
            	System.out.println("확인 버튼");
                btnOkayActionPerformed(evt);
            }
        });
		
		/////////////////// 취소버튼
		btnCancel = new JButton("취소");
		btnCancel.setFont(font);
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				System.out.println("취소 버튼");
				btnCancelActionPerformed(evt);
			}
		});
		
		

		panel = new JPanel();
//컴넌트들의 위치를 정해줍니다.
		panel.setLayout(null);

		labelName.setBounds(20,20, 100,20);
		txtName.setBounds(120,20, 150,20);

		labelAddr.setBounds(20, 45, 100,20);
		txtAddress.setBounds(120,45, 150,20);

		labelTel.setBounds(20,70, 100,20);
		txtTel.setBounds(120,70, 150, 20);

		labelRel.setBounds(20,95, 100,20);
		txtRelationShip.setBounds(120,95, 150,20);

		labelGender.setBounds(20,120, 100,20);
		comboGender.setBounds(120, 120, 150,20);
  		comboGender.setFont(new java.awt.Font("굴림", 0, 12));

		labelBirth.setBounds(20,145, 100,20);
		txtBirthDay.setBounds(120,145, 150,20);

		labelComment.setBounds(20, 170, 100,20);
		scrollComment.setBounds(120,170, 250,60);

		labelRegDate.setBounds(20, 235, 100,20);
		txtRegDate.setBounds(120,235, 150,20);
		txtRegDate.setEditable(false);

		// 컴포넌트들을 패널에 붙입니다.
		panel.add(labelName);
		panel.add(txtName);
		panel.add(labelAddr);
		panel.add(txtAddress);
		panel.add(labelTel);
		panel.add(txtTel);
		panel.add(labelRel);
		panel.add(txtRelationShip);
		panel.add(labelGender);
		panel.add(comboGender);
		panel.add(labelBirth);
		panel.add(txtBirthDay);
		panel.add(labelComment);
		panel.add(scrollComment);
		panel.add(labelRegDate);
		panel.add(txtRegDate);

		panel.add(btnOk);
		panel.add(btnCancel);

		panelBtn= new JPanel();

		panelBtn.add(btnOk);
		panelBtn.add(btnCancel);

		scrollPane = new JScrollPane(panel);

		setTitle(title);

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		getContentPane().add(panelBtn, BorderLayout.SOUTH);

		setSize(430,400);

	}

	// 화면의 타이틀과 수정여부를 셋팅하는 메쏘드입니다.
	public void set(String title, boolean editable) {
		this.setTitle(title);
		this.setEditable(editable);
	}

	// 타이틀, 수정여부, Value Object를 받아서 윈도우를 설정합니다.
	public void set(String title, boolean editable, AddressVO vo, AddressBook abook) {
		this.avo = vo;
		this.abook = abook;
		this.set(title, editable);
		this.setValue(vo);
	}
	


	// 입력, 수정시는 칼럼값을 수정 가능하도록, 조회시는 불가능하게
	// 셋팅하는 메쏘드입니다.
	private void setEditable(boolean e) {
		txtName.setEditable(e);
		txtAddress.setEditable(e);
		txtTel.setEditable(e);	
		txtRelationShip.setEditable(e);
		comboGender.setEnabled(e);
		txtBirthDay.setEditable(e);
		txtComment.setEditable(e);
	}

	/////////////////////////////////////////////// 확인버튼 선택시 작업을 정의합니다.
  	private void btnOkayActionPerformed(ActionEvent evt) {
  		if(getName().trim().length() == 0) { ///////// 이름(필수입력)
  			JOptionPane.showMessageDialog(this, "이름을 입력하세요.", "Error",
  				JOptionPane.ERROR_MESSAGE);
  			return;
  		}
		isCancel= false;
		//수정화면에서 확인버튼을 눌렀을 때와 입력화면에서 확인 버튼을 눌렀을 때 처리하기
		//아이디가 존재하면 수정 모드, 그렇지 않으면 입력 모드로 처리한다.
		// public AddressVO avo = null; // AddressBook에서 전달받은 avo
		if(avo !=null){ //////////////////////////////// 아이디가 null이 아님 = 존재하면 수정 모드
			JOptionPane.showMessageDialog(this, "수정이 완료되었습니다.","INFO", JOptionPane.INFORMATION_MESSAGE);		
			System.out.println("abook ctrl 이전 출력 : "+abook);
			try{
				AddressVO vo = new AddressVO();	
				vo.setCommand("update"); 
				
				// 다이얼로그에 입력한 값 텍스트필드 메서드로 읽어오기
				vo.setName(getName());
				vo.setAddress(getAddress());
				vo.setTelephone(getTel());
				vo.setGender(getGender());
				vo.setRelationship(getRelationShip());
				vo.setBirthday(getBirthDay());
				vo.setComments(getComments());
				vo.setRegistedate(getRegDate());
				vo.setId(avo.getId()); ///////////////// 시퀀스로 생성된 Id 가져오기
				
				AddressCtrl ctrl = new AddressCtrl(vo);
				ctrl.send(vo);
				
				System.out.println(vo);
				System.out.println("abook ctrl 이후 출력 : "+abook);
				
				if(abook != null) {
					abook.refreshData(); // 수정이 성공하면 AddressBook 클래스의 새로고침(전체조회) 메소드 호출
				}
			}catch(Exception e){
				JOptionPane.showMessageDialog(this, "수정중 에러가 발생했습니다." + e,
						"Error", JOptionPane.ERROR_MESSAGE);				
			}
		/*
		 * 아이디가 0보다 큰값이 아니면 입력모드 이다.	
		 */
		} else{ //////////////////////////////////////// 아이디가 null, 존재하지 않으면 입력 모드
			try {
				JOptionPane.showMessageDialog(this, "입력이 완료되었습니다.","INFO", JOptionPane.INFORMATION_MESSAGE);		
				AddressVO vo = new AddressVO();	
				vo.setCommand("insert");
				
				// 다이얼로그에 입력한 값 텍스트필드 메서드로 읽어오기
				vo.setName(getName());
				vo.setAddress(getAddress());
				vo.setTelephone(getTel());
				vo.setGender(getGender());
				vo.setRelationship(getRelationShip());
				vo.setBirthday(getBirthDay());
				vo.setComments(getComments());
				vo.setRegistedate(getRegDate());
				
				AddressCtrl ctrl = new AddressCtrl(vo);
				ctrl.send(vo);
				
				if(abook != null) {
				abook.refreshData(); // 입력이 성공하면 AddressBook 클래스의 새로고침(전체조회) 메소드 호출
				} 
			} catch (Exception e) {
//				JOptionPane.showMessageDialog(this, "입력중 에러가 발생했습니다." + e,
//					"Error", JOptionPane.ERROR_MESSAGE);
				JOptionPane.showMessageDialog(this, "회원가입이 완료되었습니다!\n성명과 생년월일로 로그인 해주세요.",
						"회원가입 완료", JOptionPane.PLAIN_MESSAGE);
				this.dispose(); // 회원가입 완료 시 창닫기
				aLogin.jtf_name.setText(""); // 창이 닫히면 로그인 텍스트필드 공백으로 초기화
				aLogin.jpf_bday.setText("");
			}			
		}
		setVisible(false);
	}

	public void setValue(AddressVO vo) {
		// 입력을 위한 윈도우 설정-모든값을 null로 셋팅합니다.
		if(vo == null) {
			setName("");
	  		setAddress("");
			setTel("");
			setGender("1");
			setRelationShip("");
			setBirthDay("");
			setComment("");
			setRegDate("");
		// 조회, 수정시는 Value Object에서 받은 값으로 셋팅합니다.
		} else {
			setName(vo.getName());
	  		setAddress(vo.getAddress());
			setTel(vo.getTelephone());
			setGender(vo.getGender());
			setRelationShip(vo.getRelationship());
			setBirthDay(vo.getBirthday());
			setComment(vo.getComments());
			setRegDate(vo.getRegistedate());
		}
	}

	// 취소버튼 선택시 작업을 정의합니다.
	private void btnCancelActionPerformed(ActionEvent evt) {
		isCancel= true;	
		dispose();  // 창닫기
	}
	

	// 각 컬럼의 값들을 설정하거나 읽어오는 getter/setter 메쏘드입니다.
	public String getName() { return txtName.getText(); }
	public void setName(String strName) { txtName.setText(strName); }
	public String getAddress() { return txtAddress.getText(); }
	public void setAddress(String strAddress) { txtAddress.setText(strAddress); }
	public String getTel() { return txtTel.getText(); }
	public void setTel(String strTel) { txtTel.setText(strTel); }
	public String getRelationShip() { return txtRelationShip.getText(); }
	public void setRelationShip(String strRelation) { txtRelationShip.setText(strRelation); }
	public String getBirthDay() { return txtBirthDay.getText(); }
	public void setBirthDay(String strBirth) { 	txtBirthDay.setText(strBirth); }
	public String getComments() { return txtComment.getText(); }
	public void setComments(String strCom) { 	txtComment.setText(strCom); }
	
	public String getRegDate() { return txtRegDate.getText(); }	
	public void setRegDate(String strReg) { txtRegDate.setText(strReg); }
	
	public String getGender() {
		if (comboGender.getSelectedItem().equals("남자")) return "1";
		else return "2";
	}
	public void setGender(String strGender) {
		if(strGender == null) strGender = "1";
		if (strGender.equals("1"))  comboGender.setSelectedItem("남자");
		else comboGender.setSelectedItem("여자");
	}
	public String getComment() { return txtComment.getText(); }
	public void setComment(String strComment) { txtComment.setText(strComment); }


}