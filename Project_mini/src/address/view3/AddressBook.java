package address.view3;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class AddressBook extends JFrame {
	private static final long serialVersionUID = 1L;
	DBConnectionMgr dbMgr = new DBConnectionMgr();
	// 메인화면에 사용할 컴포넌트들을 선언합니다.
    private JMenuBar menuBar;
    private JMenu menuMenu;
    private JMenu menuAbout;
	private JMenuItem menuItemConnect;
    private JMenuItem menuItemInsert;
    private JMenuItem menuItemUpdate;
    private JMenuItem menuItemDelete;
    private JMenuItem menuItemDetail;
    private JMenuItem menuItemSelectAll;
    private JMenuItem menuItemAbout;
    private JSeparator menuSeparator1;
    private JSeparator menuSeparator2;
    private JMenuItem menuItemExit;
    private JToolBar toolbar;
    private JButton btnInsert;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnDetail;
    private JButton btnSelectAll;
    private JScrollPane jScrollPane1;
    private JTable table;
	private MyTableModel myTableModel;
	private JPanel panelTimer;
	private JLabel labelTimer;

	private JOptionPane optionDlg;
	private ModifyDialog mDialog;
	private Font font;
	private String path;
	
	public LocalDateTime now;
	
	// DB작업을 중개해줄 Controller 클래스
	private AddressCtrl ctrl;
	
	public static AddressBook abook = null;
	AddressVO [] vos = null;
	
	
	
	// 메인 메소드는 AddressBook의 인스턴스를 생성하고 보여주는 일만 합니다.
    public static void main(String args[]) {
    	JFrame.setDefaultLookAndFeelDecorated(true);
        abook = new AddressBook();
        abook.initComponents();
        abook.setVisible(true);
    }

    // 생성자는 컴포넌트들을 초기화합니다.
    public AddressBook() {
        //initComponents(); // 여기 주석풀면 전체조회 안됨...... 
    }

	// 초기화 작업은 컴포넌트들의 값을 셋팅하고 배치합니다.
    public void initComponents() {
    	// DefaultTableModel을 상속받은 MyTableModel 클래스가 테이블의
    	// 데이타를 담당합니다.
		myTableModel= new MyTableModel();

		// 메뉴바, 메뉴, 메뉴 아이템을 정의합니다.
        menuBar = new JMenuBar();
        menuMenu = new JMenu();
		menuAbout= new JMenu();
		menuItemConnect= new JMenuItem();
        menuItemInsert = new JMenuItem();
        menuItemUpdate = new JMenuItem();
        menuItemDelete = new JMenuItem();
        menuItemDetail= new JMenuItem();
        menuItemSelectAll= new JMenuItem();
		menuItemAbout = new JMenuItem();
        menuSeparator1 = new JSeparator();
        menuSeparator2 = new JSeparator();
        menuItemExit = new JMenuItem();
        menuMenu.setText("메뉴");
        menuMenu.setFont(font);

        // 툴바와 이미지 버튼을 정의합니다.
        toolbar = new JToolBar();
        btnInsert = new JButton();
        btnInsert.setToolTipText("입력");
        btnUpdate = new JButton();
        btnUpdate.setToolTipText("수정");
        btnDelete = new JButton();
        btnDelete.setToolTipText("삭제");
        btnDetail = new JButton();
        btnDetail.setToolTipText("상세조회");
        btnSelectAll = new JButton("전체조회");
        btnSelectAll.setToolTipText("전체조회");
        jScrollPane1 = new JScrollPane();
        table = new JTable(myTableModel);
        JTableHeader jth = new JTableHeader();
        jScrollPane1.getViewport().setBackground(Color.white);

		
        now = LocalDateTime.now();
		String formatedNow = now.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 hh시 mm분 ss초"));
		labelTimer = new JLabel("접속 시간 : "+formatedNow); ////////////////////////////// 현재 시간
		labelTimer.setFont(font);
		panelTimer = new JPanel();
		font= new Font("굴림",0, 12);
		path = "src//images//address//";
		

		// About 화면을 출력할 대화상자 정의
		optionDlg = new JOptionPane();
		
		// 입력, 수정, 조회에 사용할 화면정의
        mDialog = new ModifyDialog(this);  ////////this는 AddressBook 자기 자신. ModifyDialog 생성자에 자기 자신의 객체를 전달함.
        mDialog.setVisible(false);

        // DB연결 메뉴아이템
		menuItemConnect.setFont(font);
		menuItemConnect.setText("DB 연결");
		menuItemConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
                System.out.println("DB 연결 메뉴");
                connectActionPerformed(evt);
			}
		});

		// 상세조회 메뉴아이템
        menuItemDetail.setFont(font);
        menuItemDetail.setText("상세조회");
        menuItemDetail.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                System.out.println("상세조회 메뉴");
                detailActionPerformed();
            }
        });
        // 전체조회 메뉴아이템
        menuItemSelectAll.setFont(font);
        menuItemSelectAll.setText("전체조회");
        menuItemSelectAll.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent evt) {
        		System.out.println("전체조회 메뉴");
        		refreshData();
        	}
        });

		// 입력 메뉴아이템
	    menuItemInsert.setFont(font);
        menuItemInsert.setText("입력");
        menuItemInsert.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                System.out.println("입력 메뉴");
                addActionPerformed(evt);
            }
        });

		// 수정 메뉴아이템
        menuItemUpdate.setFont(font);
        menuItemUpdate.setText("수정");
        menuItemUpdate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                System.out.println("수정 메뉴");
            	updateActionPerformed();
            }
        });

		// 삭제 메뉴아이템
        menuItemDelete.setFont(font);
        menuItemDelete.setText("삭제");
        menuItemDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
            	System.out.println("삭제 메뉴");
            	deleteActionPerformed();
            }
        });

		// 종료 메뉴아이템
        menuItemExit.setFont(font);
        menuItemExit.setText("종료");
        menuItemExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
            	System.out.println("종료 메뉴");
            	exitActionPerformed(evt);
            }
        });

        // 메뉴 아이템을 메뉴에 붙입니다.
		menuMenu.add(menuItemConnect);
        menuMenu.add(menuSeparator1);
		menuMenu.add(menuItemSelectAll);
		menuMenu.add(menuItemDetail);
		menuMenu.add(menuItemInsert);
		menuMenu.add(menuItemUpdate);
		menuMenu.add(menuItemDelete);
		menuMenu.add(menuSeparator2);
        menuMenu.add(menuItemExit);

 		// 첫번째 메뉴를 메뉴바에 붙입니다.
        menuBar.add(menuMenu);

		// About 메뉴
		menuAbout.setFont(font);
		menuAbout.setText("About");

		// About 메뉴아이템
		menuItemAbout.setFont(font);
		menuItemAbout.setText("About");
		menuItemAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				System.out.println("About 메뉴");
				aboutActionPerformed(evt);
			}
		});
		menuAbout.add(menuItemAbout);

		// 두번째 메뉴를 메뉴바에 붙입니다.
		menuBar.add(menuAbout);
		// 완성된 메뉴바를 프레임과 연결합니다.
		setJMenuBar(menuBar);

        // 프레임 관련 설정을 합니다.
		setTitle("주소록 데모 프로그램");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setFont(font);

		// 윈도우 리스너 설정
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
            	System.out.println("윈도우 종료");
            	System.exit(0);
            }
        });

		// 조회 아이콘
		btnDetail.setIcon(new ImageIcon(path+"detail.gif"));
        btnDetail.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
            	System.out.println("상세조회 아이콘");
            	detailActionPerformed();
            }
        });
        toolbar.add(btnDetail);
		toolbar.add(new JToolBar.Separator());

		// 입력 아이콘
        btnInsert.setIcon(new ImageIcon(path+"new.gif"));
        btnInsert.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
            	System.out.println("입력 아이콘");
            	addActionPerformed(evt);
            }
        });
		toolbar.add(btnInsert);

        // 수정 아이콘
        btnUpdate.setIcon(new ImageIcon(path+"update.gif"));
		btnUpdate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
            	System.out.println("수정 아이콘");
            	updateActionPerformed();
            }
        });
        toolbar.add(btnUpdate);

        // 삭제 아이콘
        btnDelete.setIcon(new ImageIcon(path+"delete.gif"));
		btnDelete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
            	System.out.println("삭제 아이콘");
            	deleteActionPerformed();
            }
        });
        toolbar.add(btnDelete);

        // 툴바를 컨테이너에 붙입니다.
        getContentPane().add(toolbar, java.awt.BorderLayout.NORTH);

        // 데이터 리스트가 조회될 테이블을 설정합니다.
        table.setSelectionBackground(Color.YELLOW); // 테이블 데이터 선택 시 색 변하게 하기
        table.setSelectionForeground(Color.BLUE); // 테이블 데이터 선택 시 글자색 변하게 하기
        table.setFont(font);
        
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent me) {
				if (me.getClickCount() >= 2) {
					System.out.println("데이터 더블클릭"); /////////////////////////////// 더블클릭 시 상세조회 호출
					detailActionPerformed();
				}
			}
		});
		// 테이블을 스크롤 페널에 연결하여 붙입니다.
        jScrollPane1.setViewportView(table);
        getContentPane().add(jScrollPane1, BorderLayout.CENTER);
		// 서버로부터 받아온 시간을 출력할 레이블을 붙입니다.
		panelTimer.add(labelTimer);
		getContentPane().add(panelTimer, BorderLayout.SOUTH);
        pack();

		// 데이터가 표시될 테이블의 칼럼을 설정합니다.
		myTableModel.setColumnCount(0);

		myTableModel.addColumn("아이디");
		table.getColumnModel().getColumn(0).setWidth(300);
		myTableModel.addColumn("이름");
		table.getColumnModel().getColumn(0).setWidth(300);
		myTableModel.addColumn("주소");
		table.getColumnModel().getColumn(0).setWidth(800);
		myTableModel.addColumn("전화번호");
		table.getColumnModel().getColumn(0).setWidth(500);
		

    } ///////////////////////////////////////////////////////////// end of initComponents();

	// DB연결 메뉴 선택시 작업을 정의합니다.
	private void connectActionPerformed(ActionEvent evt) {
		Connection con = null;
		try {
			con = dbMgr.getConnection();
			if(con != null) {
				JOptionPane.showMessageDialog(this, "DB 연결에 성공했습니다.\n","Info", JOptionPane.INFORMATION_MESSAGE);
				return;
			}else {
				JOptionPane.showMessageDialog(this, "DB 연결에 실패했습니다.\n","Error", JOptionPane.ERROR_MESSAGE);
				return;				
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "DB 연결에 실패했습니다.\n" + e,"Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	//////////// 상세조회 메뉴나 상세조회 아이콘 선택시 작업을 정의합니다.
	private void detailActionPerformed() {
		System.out.println("상세조회 구현");
		
		int index[] = table.getSelectedRows();
		AddressVO vo = new AddressVO();
		if(index.length == 0) {
			JOptionPane.showMessageDialog(this, "상세 조회할 데이터를 선택하세요.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		} else if(index.length > 1) {
			JOptionPane.showMessageDialog(this, "데이터를 한건만 선택하세요.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		} else {
			try {
				// 테이블에서 선택된 컬럼의 id를 읽어옵니다.
//				Integer id= (Integer)myTableModel.getValueAt(index[0], 0);
				Integer id = Integer.parseInt
				(myTableModel.getValueAt(index[0], 0).toString());				
				AddressVO newVo = null;
				for(int i=0;i<vos.length;i++) {
					if(id == vos[i].getId()) {
						newVo = new AddressVO(vos[i].getName(),vos[i].getAddress(),vos[i].getTelephone()
								             ,vos[i].getGender(),vos[i].getRelationship(),vos[i].getBirthday()
								             ,vos[i].getComments(),vos[i].getRegistedate(), vos[i].getId());
						newVo.setCommand("select");
					}
				}
				ctrl = new AddressCtrl(newVo);
				ctrl.send(newVo);
				
				// public void set(String title, boolean editable, AddressVO vo, AddressBook abook)
				
				////////////// 상세 조회는 조회만 가능하도록 editable을 false로 준다.
				mDialog.set("상세조회", false, newVo, abook); 
				mDialog.setVisible(true);
				
			} catch (Exception e) {
				JOptionPane.showMessageDialog(this, "데이터를 가져오는 중 발생했습니다."+e.toString(),"Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	// 입력 메뉴나 입력 아이콘 선택시 작업을 정의합니다.
	private void addActionPerformed(ActionEvent evt) {
		System.out.println("입력하기");	
		mDialog.set("입력", true, null,abook);   ////////////////////// 데이터가 없는 입력창이므로 AddressVO는 null을 넘겨서 mDialog호출
		mDialog.setVisible(true);
	}

	// 수정 메뉴나 수정 아이콘 선택시 작업을 정의합니다.
	public void updateActionPerformed() {
		System.out.println("수정하기");
		int index[] = table.getSelectedRows();
		AddressVO vo = new AddressVO();
		if(index.length == 0) {
			JOptionPane.showMessageDialog(this, "수정할 데이터를 선택하세요.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		} else if(index.length > 1) {
			JOptionPane.showMessageDialog(this, "데이터를 한건만 선택하세요.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		} else {
			try {
				// 테이블에서 선택된 컬럼의 id를 읽어옵니다.
//				Integer id= (Integer)myTableModel.getValueAt(index[0], 0);
				Integer id = Integer.parseInt
				(myTableModel.getValueAt(index[0], 0).toString());				
				AddressVO newVo = null;
				for(int i=1;i<vos.length;++i) {
					if(id == vos[i].getId()) {
						newVo = new AddressVO(vos[i].getName(),vos[i].getAddress(),vos[i].getTelephone(),vos[i].getGender()
								             ,vos[i].getRelationship(),vos[i].getBirthday(),vos[i].getComments()
								             ,vos[i].getRegistedate(), vos[i].getId());
					}
				}
				mDialog.set("수정", true, newVo, abook); ////////////// DefaultTableModel에 입력되었던 값을 newVO에 담아 넘겨서 mDialog호출
				mDialog.setVisible(true);
			} catch (Exception e) {
				JOptionPane.showMessageDialog(this, "데이터를 가져오는 중 발생했습니다."+e.toString(),"Error", JOptionPane.ERROR_MESSAGE);
			}
		}		
	}

	// 삭제 메뉴나 삭제 아이콘 선택시 작업을 정의합니다.
	private void deleteActionPerformed() {
		System.out.println("삭제하기");
		int index[] = table.getSelectedRows();
		AddressVO vo = new AddressVO();
		if(index.length == 0) {
			JOptionPane.showMessageDialog(this, "삭제할 데이터를 선택하세요.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		} else if(index.length > 1) {
			JOptionPane.showMessageDialog(this, "데이터를 한건만 선택하세요.", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		} else {
			try {
				int choose = JOptionPane.showConfirmDialog(this, "선택한 데이터를 삭제하시겠습니까?","INFO", JOptionPane.YES_NO_OPTION);
				if(choose==JOptionPane.YES_OPTION) {   ///////////////////////////////// yes 옵션 선택 시 삭제 진행
					// 테이블에서 선택된 컬럼의 id를 읽어옵니다.
					Integer id = Integer.parseInt
					(myTableModel.getValueAt(index[0], 0).toString());				
					AddressVO newVo = null;
					for(int i=0;i<vos.length;i++) {  //AddressVO [] vos
						if(id == vos[i].getId()) {
							newVo = new AddressVO(vos[i].getName(),vos[i].getAddress(),vos[i].getTelephone()
									             ,vos[i].getGender(),vos[i].getRelationship(),vos[i].getBirthday()
									             ,vos[i].getComments(),vos[i].getRegistedate(), vos[i].getId());
							newVo.setCommand("delete");
						}
					}
					ctrl = new AddressCtrl(newVo);
					ctrl.send(newVo);
					
					refreshData(); // 삭제 성공하면 새로고침(전체조회) 메소드 호출
					
				} else { /////////////////////////////// no 옵션 선택 시 삭제 수행하지 않음
					return;
				}
				
			} catch (Exception e) {
				JOptionPane.showMessageDialog(this, "데이터를 가져오는 중 발생했습니다."+e.toString(),"Error", JOptionPane.ERROR_MESSAGE);
			}
		}		
		
	}

	// 종료 메뉴 선택시 작업을 정의합니다.
	private void exitActionPerformed(ActionEvent evt) {
		System.exit(0);
	}

	// About 메뉴 선택시 작업을 정의합니다.
	private void aboutActionPerformed(ActionEvent evt) {
		optionDlg.setFont(font);
		JOptionPane.showMessageDialog(this, "주소록 데모 프로그램 1.0","About", JOptionPane.INFORMATION_MESSAGE);
	}

	// 전체 데이터를 다시 조회합니다. (새로고침이 일어났을 때 화면을 갱신)
	public void refreshData() {
		System.out.println("전체 데이터를 다시 조회합니다.");
		AddressVO vo = new AddressVO();
		vo.setCommand("selectall");
		ctrl = new AddressCtrl(vo);	// 전체조회의 경우 ROW의 숫자가 여러개 이므로 배열에 담음
		// Controller에서 넘겨 받은 전체 데이터를 테이블에 셋팅합니다.
		vos = ctrl.send();	
		
		while(myTableModel.getRowCount() > 0) { //기존에 조회된 결과 즉 목록을 삭제하기
			myTableModel.removeRow(0); // 파라미터에 0을 주어서 테이블의 인덱스가 바뀌는 문제를 해결함
			}
		
			for (int i = 0; i < vos.length; i++) {
				Vector<Object> oneRow = new Vector<>();
				oneRow.add(vos[i].getId());
				oneRow.add(vos[i].getName());
				oneRow.add(vos[i].getAddress());
				oneRow.add(vos[i].getTelephone());			
				myTableModel.addRow(oneRow);
				}
		}/////////////////////////////// end of refreshData()
	}

//		if(vos == null || vos.length == 0) {
//			vos = new AddressVO[2];	
//			AddressVO rvo = new AddressVO("이순신","서울시 마포구 공덕동","010-555-6677","1"
//					                     ,"고교동창","1990-05-28","Back-End개발자","2022-03-15",1);
//			vos[0] = rvo;
		
//			rvo = new AddressVO("강감찬","서울시 영등포구 당산동","010-777-6677","1"
//                    ,"대학동창","1992-01-28","Back-End개발자","2022-01-25",2);
//			vos[1] = rvo;
			
//		}


// 데이터를 보여줄 테이블의 실제 데이터를 관리하는 클래스입니다.
class MyTableModel extends DefaultTableModel {

	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

}
