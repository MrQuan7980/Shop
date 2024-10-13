/*

    Các file trong app Shop (java)

    * activity includes:
        + SignInActivity: create_account_sign_in (user) and sign_in (user, shop)
        + Reset_password: check email and check phone -> True: ->  Change_password_activity ? False: stop
        + Change_password_: receive phone -> check phone: True -> existence (update new password) ? False: stop
        + Main_activity includes:
            <item>
                - Home_frames:
                - Search_frames:
                - Favorite_frames:
                - Notification_frames:
                - Person_frame:
            </item>

    * database includes:
        + use Firebase: là cơ sở dữ liệu NoSQl (bất quy tắc) và có thể đồng bộ hóa dữ liệu người dùng theo thời gian thực
        + Preference:
            - Lưu trữ theo kiểu SharedPreferences: lưu trữ dữ liệu đơn giản và lưu trữ dữ liệu bằng (key - value)
                + key: là một giá trị duy nhất xác định dữ liệu (giống như cột)
                + value: là đối tượng mà chúng ta muốn lưu trữ
            - Các ví dụ của SharedPreferences nên nhớ:
                + putBoolean(String key, Boolean value): Lưu vào key và giá trị đúng sai (true - false)
                + getBoolean(String key): lấy ra để kiểm tra trong SharedPreferences
                + putString(String key, String value): lưu và SharedPreferences va giống như là lưu vào bảng vậy
                + getString(String key): lấy ra giá trị key đã lưu
                + clear(): Xóa các cặp key - value trong SharedPreferences

        + Constant: đại diện cho các keys và truy xuất dữ liệu dễ dàng hơn giống như các cột
            + static: có thể truy xuất vào mà không cần tạo 1 thực thể
            + final: là giá trị thực thể không thay đổi và không thể bị gán mới khi nó được định nghĩa final

    * adapter

    * shop includes
        + ShopActivity: Hiển thị danh sách hàng hóa shop đăng lên và sẽ hiển thih theo số lượng đã bán (bán chạy...) từ nhiều nhất -> bé nhất
            + Button: Edit -> (Shop -> Click -> Edit_product_activity)
            + Button: Delete -> (Shop -> Click -> checkId -> (true or false) : True -> ShopActivity ? False -> notification

        + Add_product_activity: Them sản phẩm (ảnh, tiêu đề, số lượng, loại hàng, giá, đã bán...)
        + Edit_product_activity: Sửa sản phẩm dựa vào view đã tạo bên trên Add_product_activity : True -> ShopActivity ? False -> check (notification)
        + Sales_revenue_activity: Xem doanh thu và hàng hóa đã bán của shop


        + Shop_SignUp: Tạo tài khoản bán hàng nếu check: True -> SignInActivity ? False -> Stop (notification)

    * frames includes:
        - Home
        - Search
        - Favorite
        - Notification
        - Person

    * adapter includes:

    * object: huướng đối tượng là các đối tượng tham gia bên trong cái dự án này và sẽ dùng để gọi các đối tượng đó vào trong Adapter và hiển thị lên dễ hơn



    Các tập tin quan trọng
    - AndroidManifest: Dùng để cài đặt cho phép truy cập vào internet và sẽ là bắt đầu của dự án
    - java: là ngôn ngữ code chính brojeck này
    - drawable: Tạo các view để hiển thị bắt mắt hơn - lưu trữ ảnh - icon
    - menu: nơi hiển thị item để chuyển đổi giữa các frames
    - layout: là nơi lưu trữ các from vd: sign_in.xml, activity_activity.xml...
    - raw: nơi lưu trữ các video
    - mipmap
    - value:
        + colors: lưu trữ màu sắc
        + strings: lưu trữ những text dễ đọc bên trong và lưu vào
    - build.gradle.kts (Project: name_app): là hiển thị verson và kết nối với firebase
    - build.gradle.kts (Module: app): Thêm thư viện cho firebase, giao diện, viewbiding...

        Vd: implementation("com.intuit.ssp:ssp-android:1.1.0")

            implementation("com.intuit.sdp:sdp-android:1.1.0")

            implementation("com.makeramen:roundedimageview:2.3.0")



    * Tạo ra item_container_product_shop.xml: để hiển thị sản phẩm shop đăng bán -> đưa vào RecyclerView (home_shop)


    * Tạo ra item_container_product_user.xml: để hiển thị sản phẩm bên user -> RecyclerView (home)

    * Tạo ra một đối tượng là Product




    Hướng đối tượng Object

    là mô hình lập trình áp dụng trên quy tắc gần gũi với thực tế và thế giới thật

    * Tính đóng gói
        - Đóng gói giúp trở thành một đối tượng, giúp bảo vệ dữ liệu không bị truy cập từ bên ngoài (Thay đổi qua getTer, setter)
        - Quản lí truy cập public, private, protected


        Vd:
            public class Person()
            {
                private String name;
                private int age;
            }

            public Person(String name, int age)
            {
                this.name = name;
                this.age = age;
            }

    * static: khi tạo static các lớp khác co thế gội đến thành phần, biến và không cần tạo đối tượng mới
    * final: đánh dấu giá trị không thay đổi


    * Tính kế thừa
        - Cho phép lớp con kế thừa những thuộc tính của lớp cha. Giups tái sử dụng mã, giảm trùng lặp

    * Tính Đa hình
        - Cho phép sử dụng các đối tượng của các lớp khác nhau thông qua mộ giao diện hoặc một phương thức, mỗi lớp sẽ thực hiện một chức năng thwo cách riêng của nó

    * Tính trừu tượng:
        - Giảm thiểu sự phức tạp của các tính năng quan trọng của các đối tượng, che giấu các chi tiết không cần thiết


    * Abstract:
    -Class (Lớp trừu tượng)
        Class abstract: không thể tạo đối tượng mới trong đó (class cha)
        Class: Có thể tạo đối tượng trong hàm main

    - Abstract:
        - Khai báo phương thức abstract: dể bắt buộc các lớp con phải kế thừa và luôn có phương thức đã khai báo lớp cha


    * Interface: là bậc cao hơn Abstract (lớp giao diện)
        - Khai báo trong đây là một phương thức Abstract và sử dụng implement để gọi phương thức Interface



    * Class: + class là một khuôn mẫu mà object phải có
             + Trong class là các phương thức, thược tính mà các đối tượng sẽ có
             + Lớp không chứa không gian lưu trữ bộ nhớ cho đến khi bạn tạo một đối tương từ lớp

    * Object (Đối tượng): Là một đối tượng được tạo từ Class và một đối tượng sẽ có các phương thức thuộc tính

    Tóm lại:
        + Class là nơi định nghĩa các thuộc tính và phương thức
        + Object là nơi thưc thi các thuộc tính và phương thức tùy vào quyền cho truy cập của class

    Constructor:
        + khởi tạo đối tượng cảu lớp đó
        + được tạo để gán giá trị cho các thuộc tính mà đối tượng tạo ra
            - Có đối: khởi tạo được giá trị mới ngay khi gọi hàm
            - Không đối: phải gọi hàm để gán giá trị


    * Thành phần cần phải nhớ:
        - Khi khai báo private String color (Thuộc tính or biến)
        - Khai báo phương thức là phải có hành động cụ thể (public void add())
        - Hàm khởi tạo (Constructor)


    * Hoạt động của Activity:
        - Cung câp cửa sổ xây dựng giao diện người dùng 
        - Vào Activity launched -> onCreate() -> onStart() -> onResume() -> onPause() -> onStop() -> onDestroy()




























    * các tạo 1 item_view hiển thị lên Adapter

        + tạo một item_view (XML)
            - hiển thị lên id và name

        + Tạo một AdapterUser để kết nối dữ liệu trong layout
            - Tạo 1 class để hiển thị dữ liệu lên view

        + Thêm một đối tượng để gán dữ liệu Vd
            VD: User {
                String id, name;
            }
        + Hiển thị Adapter lên Activity:
            - Gọi List<User> listUser;
            - Gọi lại Adapter adapter;

            Gọi User user = new User()

            user.id =
            user.name =

            listUser.add(user);

            adapter = new AdapterUser (listUser)

            Hiển thị lên RecyclerView.setAdapter(adapter)


    - Gồm những loại RecyclerView :
        + linearLayoutManager: hiển thị theo chiều dọc ngang
        + GridLayoutManager: Hiển thị theo dạng lưới theo nhiều hàng nhiều cột
        + StaggeredGridLayoutManager: Hiển thị dữ liệu theo dạng không đồng nhất và dạng xếp chồng


 */