/*


I. Cấu trúc dữ liệu cơ bản

1. Array

    * Bài 1: Viết một chương trình để đảo ngược một mảng

    public static void main(string[] args)
    {
        Scanner sc = new Scanner(System.in);
        int n; so phần tử của mảng
        int [] array;
        array = new int[n];

        System.out.print("Nhập mảng : ");
        n = sc.nextInt();

        for (int i = 0;i < n;i ++)
        {
            System.out.print("Nhập phần tử thứ " + (i + 1) + " : ");
            array[i] = sc.nextInt();
        }

    }
    public static void hamdaonguoc (int[] array)
    {
        int phan_tu_dau = 0;
        int phan_tu_cuoi = array.length - 1;


        if (phan_tu_dau < phan_tu_cuoi)
        {
            int tam = array[phan_tu_dau];
            array[phan_tu_dau] = array[phan_tu_cuoi];
            array[phan_tu_cuoi] = tam;
        }

        phan_tu_dau++;
        phan_tu_cuoi--;
    }



    + Bài 2: Tìm phần tử lớn thứ hai trong mảng

    public static void main(String[] args)
    {
        Scanner sc = new Scanner(System.in);
        int n;

        System.out.print("Nhập số phần tử của mảng : ");
        n = sc.nextInt();

        int [] array = new int[n];

        System.out.print("Nhập mảng : ");

        for (int i = 0;i < n;i ++)
        {
            System.out.print("Nhập phần tử thứ " + (i + 1) + " : ");
            array[i] = sc.nextInt();
        }

        int max = Integer.MIN_VALUE;
        int can_max = Integer.MIN_VALUE;
        for (int i = 0;i < n;i++)
        {
            if (array[i] > max)
            {
                can_max = max;
                max = array[i];
            }
            else if (array[i] > can_max && array[i] != max)
            {
                can_max = array[i];
            }
        }
        System.out.println("Phần tử lớn thứ hai là : " + can_max);
    }


    + Bài 3: Kiểm tra giá trị cuối có phải là đối xứng không

    pblic static boolean kiemtra(int[] array)
    {
        int dau = 0;
        int cuoi = array.length - 1;

        where (dau < cuoi)
        {
            if (array[dau] != array[cuoi])
            {
                return false;
            }

            dau++;
            cuoi--;
        }

        return true;
    }

2. Ngăn xếp (stack)
    + Bài 1: Triển khai một ngăn xếp dùng mảng và thực hiện thao tác đẩy (push), bật (pop) và kiểm tra ngăn xếp rỗng


    + Bài 2: Kiểm tra tính hợp lệ của một chuỗi ngoặc đơn (Vd: ((())), [()])

4. Hàng đợi

    + Bài 1: Triển khai hàng đợi sữ dụng mảng và thực hiện thao tác thêm (qnqueue) và xóa (dequeue)
    + Bài 2: Mô phỏng hàn đợi tại quầy bán evs

5. Cây (Tree)

    + Bài 1: Triển khai cây nhị phân và thực hiện duyệt cây theo thứ tự trước (peorder), giữa (inorder), sau (postorder)
    + Bài 2: Tìm chiều cao của một cây nhị phân

6. Đồ thị

    + Bài 1: Triển khai đồ thị bằng danh sách kề và ma trận kề
    + Câu 2: Tực hiện tìm kiếm theo chiều rộng (BFS) và tìm kiếm theo chiều sâu (DFS) trên đồ thị


II. Thuật toán cơ bản








































 */