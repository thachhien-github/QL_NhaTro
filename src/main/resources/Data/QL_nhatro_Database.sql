------------------------------------------------------------
-- 1. TẠO DATABASE
------------------------------------------------------------
IF DB_ID('QL_nhatro') IS NULL
    CREATE DATABASE QL_nhatro;
GO
USE QL_nhatro;
GO

------------------------------------------------------------
-- 2. TẠO BẢNG
------------------------------------------------------------
-- ===== Bảng Phòng =====
CREATE TABLE Phong (
    MaPhong    VARCHAR(10)    NOT NULL PRIMARY KEY,
    TenPhong   NVARCHAR(50)   NOT NULL,
    LoaiPhong  NVARCHAR(50),
    GiaThue    DECIMAL(18,2)  NOT NULL,
    TinhTrang  NVARCHAR(20)   NOT NULL DEFAULT N'Trống'
);
GO

-- ===== Bảng Khách Thuê (1-1 với Phòng) =====
CREATE TABLE KhachThue (
    MaKT     VARCHAR(10)    NOT NULL PRIMARY KEY,
    HoTen    NVARCHAR(100)  NOT NULL,
    GioiTinh NVARCHAR(10),
    NgaySinh DATE,
    CCCD     NVARCHAR(20),
    SDT      NVARCHAR(15),
    DiaChi   NVARCHAR(200),
    MaPhong  VARCHAR(10)    NOT NULL UNIQUE,
    CONSTRAINT FK_KhachThue_Phong FOREIGN KEY (MaPhong) REFERENCES Phong(MaPhong)
);
GO

-- ===== Bảng Xe (1 khách có thể có nhiều xe) =====
CREATE TABLE Xe (
    MaXe     VARCHAR(10)    NOT NULL PRIMARY KEY,
    MaKT     VARCHAR(10)    NOT NULL,
    BienSo   NVARCHAR(20)   NOT NULL UNIQUE,
    LoaiXe   NVARCHAR(50),
    PhiGiuXe DECIMAL(18,2)  NOT NULL,
    CONSTRAINT FK_Xe_KhachThue FOREIGN KEY (MaKT) REFERENCES KhachThue(MaKT)
);
GO

-- ===== Bảng Chỉ số điện nước =====
CREATE TABLE ChiSoDN (
    MaPhong    VARCHAR(10)   NOT NULL,
    Thang      INT           NOT NULL,
    Nam        INT           NOT NULL,
    DienCu     INT           NOT NULL,
    DienMoi    INT           NOT NULL,
    NuocCu     INT           NOT NULL,
    NuocMoi    INT           NOT NULL,
    ChiSoDien  AS (DienMoi - DienCu) PERSISTED,
    ChiSoNuoc  AS (NuocMoi - NuocCu) PERSISTED,
    CONSTRAINT PK_ChiSoDN PRIMARY KEY (MaPhong, Thang, Nam),
    CONSTRAINT FK_ChiSoDN_Phong FOREIGN KEY (MaPhong) REFERENCES Phong(MaPhong)
);
GO

-- ===== Bảng Hợp Đồng (1 phòng 1 khách) =====
CREATE TABLE HopDong (
    MaHD        VARCHAR(10)   NOT NULL PRIMARY KEY,
    MaKT        VARCHAR(10)   NOT NULL UNIQUE,   -- 1 khách chỉ 1 hợp đồng
    MaPhong     VARCHAR(10)   NOT NULL UNIQUE,   -- 1 phòng chỉ 1 hợp đồng
    NgayBatDau  DATE          NOT NULL,
    NgayKetThuc DATE          NULL,
    CONSTRAINT FK_HopDong_KhachThue FOREIGN KEY (MaKT) REFERENCES KhachThue(MaKT),
    CONSTRAINT FK_HopDong_Phong     FOREIGN KEY (MaPhong) REFERENCES Phong(MaPhong)
);
GO

-- ===== Bảng Hóa đơn =====
CREATE TABLE HoaDon (
    MaHDon INT IDENTITY(1,1) PRIMARY KEY,   -- Tự tăng
    MaPhong   VARCHAR(10)   NOT NULL,
    Thang     INT           NOT NULL,
    Nam       INT           NOT NULL,
    TienPhong DECIMAL(18,2) NOT NULL,
    TienDien  DECIMAL(18,2) NOT NULL,
    TienNuoc  DECIMAL(18,2) NOT NULL,
    TienXe    DECIMAL(18,2) NOT NULL,
    TongTien  AS (TienPhong + TienDien + TienNuoc + TienXe) PERSISTED,
    CONSTRAINT UQ_HoaDon UNIQUE (MaPhong, Thang, Nam),
    CONSTRAINT FK_HoaDon_Phong FOREIGN KEY (MaPhong) REFERENCES Phong(MaPhong)
);
GO

------------------------------------------------------------
-- 3. TRIGGER CẬP NHẬT TÌNH TRẠNG PHÒNG
------------------------------------------------------------
CREATE TRIGGER trg_UpdateTinhTrangPhong
ON HopDong
AFTER INSERT, UPDATE, DELETE
AS
BEGIN
    UPDATE Phong
    SET TinhTrang = CASE
        WHEN EXISTS (
            SELECT 1
            FROM HopDong hd
            WHERE hd.MaPhong = Phong.MaPhong
              AND (hd.NgayKetThuc IS NULL OR hd.NgayKetThuc >= GETDATE())
        )
        THEN N'Đã thuê'
        ELSE N'Trống'
    END;
END;
GO

------------------------------------------------------------
-- 4. STORED PROCEDURE CRUD
------------------------------------------------------------

-- ===== PHÒNG =====
CREATE PROCEDURE sp_ThemPhong
    @MaPhong VARCHAR(10),
    @TenPhong NVARCHAR(50),
    @LoaiPhong NVARCHAR(50),
    @GiaThue DECIMAL(18,2)
AS
BEGIN
    INSERT INTO Phong(MaPhong, TenPhong, LoaiPhong, GiaThue)
    VALUES(@MaPhong, @TenPhong, @LoaiPhong, @GiaThue);
END;
GO

CREATE PROCEDURE sp_SuaPhong
    @MaPhong VARCHAR(10),
    @TenPhong NVARCHAR(50),
    @LoaiPhong NVARCHAR(50),
    @GiaThue DECIMAL(18,2)
AS
BEGIN
    UPDATE Phong
    SET TenPhong=@TenPhong, LoaiPhong=@LoaiPhong, GiaThue=@GiaThue
    WHERE MaPhong=@MaPhong;
END;
GO

CREATE PROCEDURE sp_XoaPhong
    @MaPhong VARCHAR(10)
AS
BEGIN
    DELETE FROM Phong WHERE MaPhong=@MaPhong;
END;
GO

-- ===== KHÁCH THUÊ =====
CREATE PROCEDURE sp_ThemKhach
    @MaKT VARCHAR(10),
    @HoTen NVARCHAR(100),
    @GioiTinh NVARCHAR(10),
    @NgaySinh DATE,
    @CCCD NVARCHAR(20),
    @SDT NVARCHAR(15),
    @DiaChi NVARCHAR(200),
    @MaPhong VARCHAR(10)
AS
BEGIN
    INSERT INTO KhachThue(MaKT, HoTen, GioiTinh, NgaySinh, CCCD, SDT, DiaChi, MaPhong)
    VALUES(@MaKT,@HoTen,@GioiTinh,@NgaySinh,@CCCD,@SDT,@DiaChi,@MaPhong);
END;
GO

CREATE PROCEDURE sp_SuaKhach
    @MaKT VARCHAR(10),
    @HoTen NVARCHAR(100),
    @GioiTinh NVARCHAR(10),
    @NgaySinh DATE,
    @CCCD NVARCHAR(20),
    @SDT NVARCHAR(15),
    @DiaChi NVARCHAR(200)
AS
BEGIN
    UPDATE KhachThue
    SET HoTen=@HoTen, GioiTinh=@GioiTinh, NgaySinh=@NgaySinh,
        CCCD=@CCCD, SDT=@SDT, DiaChi=@DiaChi
    WHERE MaKT=@MaKT;
END;
GO

CREATE OR ALTER PROCEDURE sp_XoaKhach
    @MaKT VARCHAR(10)
AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @MaPhong VARCHAR(10);

    -- Lấy mã phòng từ hợp đồng của khách thuê
    SELECT TOP 1 @MaPhong = MaPhong
    FROM HopDong
    WHERE MaKT = @MaKT;

    -- Xóa xe
    DELETE FROM Xe WHERE MaKT = @MaKT;

    -- Xóa hóa đơn (theo phòng)
    IF @MaPhong IS NOT NULL
    BEGIN
        DELETE FROM HoaDon WHERE MaPhong = @MaPhong;
    END

    -- Xóa hợp đồng
    DELETE FROM HopDong WHERE MaKT = @MaKT;

    -- Xóa khách thuê
    DELETE FROM KhachThue WHERE MaKT = @MaKT;

    -- Cập nhật trạng thái phòng về Trống
    IF @MaPhong IS NOT NULL
    BEGIN
        UPDATE Phong SET TinhTrang = N'Trống'
        WHERE MaPhong = @MaPhong;
    END
END;
GO

-- ===== HỢP ĐỒNG =====
CREATE PROCEDURE sp_ThemHopDong
    @MaHD VARCHAR(10),
    @MaKT VARCHAR(10),
    @MaPhong VARCHAR(10),
    @NgayBatDau DATE,
    @NgayKetThuc DATE = NULL
AS
BEGIN
    INSERT INTO HopDong(MaHD, MaKT, MaPhong, NgayBatDau, NgayKetThuc)
    VALUES(@MaHD,@MaKT,@MaPhong,@NgayBatDau,@NgayKetThuc);
END;
GO

CREATE PROCEDURE sp_SuaHopDong
    @MaHD VARCHAR(10),
    @NgayKetThuc DATE
AS
BEGIN
    UPDATE HopDong
    SET NgayKetThuc=@NgayKetThuc
    WHERE MaHD=@MaHD;
END;
GO

CREATE PROCEDURE sp_XoaHopDong
    @MaHD VARCHAR(10)
AS
BEGIN
    DELETE FROM HopDong WHERE MaHD=@MaHD;
END;
GO


-- ===== XE =====
CREATE PROCEDURE sp_ThemXe
    @MaXe VARCHAR(10),
    @MaKT VARCHAR(10),
    @BienSo NVARCHAR(20),
    @LoaiXe NVARCHAR(50),
    @PhiGiuXe DECIMAL(18,2)
AS
BEGIN
    INSERT INTO Xe(MaXe, MaKT, BienSo, LoaiXe, PhiGiuXe)
    VALUES(@MaXe,@MaKT,@BienSo,@LoaiXe,@PhiGiuXe);
END;
GO

CREATE PROCEDURE sp_SuaXe
    @MaXe VARCHAR(10),
    @BienSo NVARCHAR(20),
    @LoaiXe NVARCHAR(50),
    @PhiGiuXe DECIMAL(18,2)
AS
BEGIN
    UPDATE Xe
    SET BienSo=@BienSo, LoaiXe=@LoaiXe, PhiGiuXe=@PhiGiuXe
    WHERE MaXe=@MaXe;
END;
GO

CREATE PROCEDURE sp_XoaXe
    @MaXe VARCHAR(10)
AS
BEGIN
    DELETE FROM Xe WHERE MaXe=@MaXe;
END;
GO


-- ===== HÓA ĐƠN =====
CREATE PROCEDURE sp_ThemHoaDon
    @MaHDon VARCHAR(10),
    @MaPhong VARCHAR(10),
    @Thang INT,
    @Nam INT,
    @TienPhong DECIMAL(18,2),
    @TienDien DECIMAL(18,2),
    @TienNuoc DECIMAL(18,2),
    @TienXe DECIMAL(18,2)
AS
BEGIN
    INSERT INTO HoaDon(MaHDon, MaPhong, Thang, Nam, TienPhong, TienDien, TienNuoc, TienXe)
    VALUES(@MaHDon,@MaPhong,@Thang,@Nam,@TienPhong,@TienDien,@TienNuoc,@TienXe);
END;
GO

-- ===== CHỈ SỐ ĐIỆN NƯỚC =====
CREATE PROCEDURE sp_ThemChiSo
    @MaPhong VARCHAR(10),
    @Thang INT,
    @Nam INT,
    @DienCu INT,
    @DienMoi INT,
    @NuocCu INT,
    @NuocMoi INT
AS
BEGIN
    INSERT INTO ChiSoDN(MaPhong, Thang, Nam, DienCu, DienMoi, NuocCu, NuocMoi)
    VALUES(@MaPhong,@Thang,@Nam,@DienCu,@DienMoi,@NuocCu,@NuocMoi);
END;
GO

CREATE PROCEDURE sp_XemChiSo
AS
BEGIN
    SELECT * FROM ChiSoDN;
END;
GO

------------------------------------------------------------
-- VIEW TỔNG HỢP HÓA ĐƠN CHI TIẾT
------------------------------------------------------------
CREATE OR ALTER VIEW v_HoaDonChiTiet
AS
SELECT 
    hd.MaHDon,
    p.TenPhong,
    hd.Thang,
    hd.Nam,
    hd.TienPhong,
    hd.TienDien,
    hd.TienNuoc,
    hd.TienXe,
    (hd.TienPhong + hd.TienDien + hd.TienNuoc + hd.TienXe) AS TongTien
FROM HoaDon hd
INNER JOIN Phong p ON hd.MaPhong = p.MaPhong;
GO

CREATE OR ALTER PROCEDURE sp_TaoHoaDonTuDong
    @MaPhong VARCHAR(10),
    @Thang INT,
    @Nam INT
AS
BEGIN
    SET NOCOUNT ON;

    DECLARE @TienPhong DECIMAL(18,2),
            @TienDien DECIMAL(18,2),
            @TienNuoc DECIMAL(18,2),
            @TienXe   DECIMAL(18,2);

    -- Lấy tiền phòng
    SELECT @TienPhong = GiaThue 
    FROM Phong 
    WHERE MaPhong = @MaPhong;

    -- Lấy tiền điện, nước
    SELECT @TienDien = (cs.DienMoi - cs.DienCu) * 3500,
           @TienNuoc = (cs.NuocMoi - cs.NuocCu) * 15000
    FROM ChiSoDN cs
    WHERE cs.MaPhong = @MaPhong 
      AND cs.Thang = @Thang 
      AND cs.Nam = @Nam;

    -- Lấy phí giữ xe
    SELECT @TienXe = ISNULL(SUM(x.PhiGiuXe),0)
    FROM Xe x
    JOIN KhachThue kt ON kt.MaKT = x.MaKT
    WHERE kt.MaPhong = @MaPhong;

    -- Insert hóa đơn (đúng cú pháp với VALUES và @parameter)
    INSERT INTO HoaDon (MaPhong, Thang, Nam, TienPhong, TienDien, TienNuoc, TienXe)
    VALUES (@MaPhong, @Thang, @Nam, @TienPhong, @TienDien, @TienNuoc, @TienXe);
END
GO



------------------------------------------------------------
-- 5. DỮ LIỆU MẪU
------------------------------------------------------------

-- ===== PHÒNG =====
INSERT INTO Phong(MaPhong, TenPhong, LoaiPhong, GiaThue)
VALUES
('P101', N'Phòng 101', N'Đơn', 1500000),
('P102', N'Phòng 102', N'Đơn', 1500000),
('P201', N'Phòng 201', N'Đôi', 2500000),
('P202', N'Phòng 202', N'Đôi', 2500000);

-- ===== KHÁCH THUÊ =====
INSERT INTO KhachThue(MaKT, HoTen, GioiTinh, NgaySinh, CCCD, SDT, DiaChi, MaPhong)
VALUES
('KT01', N'Nguyễn Văn A', N'Nam', '1999-05-12', '123456789', '0912345678', N'Hà Nội', 'P101'),
('KT02', N'Trần Thị B', N'Nữ',  '2000-08-22', '987654321', '0987654321', N'Hải Phòng', 'P102');

-- ===== HỢP ĐỒNG =====
INSERT INTO HopDong(MaHD, MaKT, MaPhong, NgayBatDau, NgayKetThuc)
VALUES
('HD01', 'KT01', 'P101', '2025-01-01', NULL),
('HD02', 'KT02', 'P102', '2025-02-01', NULL);

-- ===== XE =====
INSERT INTO Xe(MaXe, MaKT, BienSo, LoaiXe, PhiGiuXe)
VALUES
('X01', 'KT01', N'29A-12345', N'Xe số', 100000),
('X02', 'KT02', N'15B-67890', N'Xe tay ga', 120000);

-- ===== CHỈ SỐ ĐIỆN NƯỚC =====
INSERT INTO ChiSoDN(MaPhong, Thang, Nam, DienCu, DienMoi, NuocCu, NuocMoi)
VALUES
('P101', 8, 2025, 100, 130, 50, 65),
('P102', 8, 2025, 200, 240, 80, 100);

-- ===== HÓA ĐƠN =====
INSERT INTO HoaDon(MaPhong, Thang, Nam, TienPhong, TienDien, TienNuoc, TienXe)
VALUES
('P101', 8, 2025, 1500000, 30*3500, 15*15000, 100000),
('P102', 8, 2025, 1500000, 40*3500, 20*15000, 120000);


EXEC sp_XemChiSo;

SELECT * FROM v_HoaDonChiTiet;

SELECT * 
FROM v_HoaDonChiTiet
WHERE Thang = 8 AND Nam = 2025;


