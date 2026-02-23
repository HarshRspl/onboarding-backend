CREATE TABLE IF NOT EXISTS hr_executives (
  id             BIGINT        PRIMARY KEY AUTO_INCREMENT,
  name           VARCHAR(100)  NOT NULL,
  email          VARCHAR(150)  NOT NULL UNIQUE,
  phone          VARCHAR(15),
  role           VARCHAR(100),
  employee_code  VARCHAR(20)   UNIQUE,
  active         TINYINT(1)    NOT NULL DEFAULT 1,
  created_at     DATETIME      DEFAULT CURRENT_TIMESTAMP,
  updated_at     DATETIME      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS candidates (
  id                 BIGINT        PRIMARY KEY AUTO_INCREMENT,
  employee_name      VARCHAR(100)  NOT NULL,
  aadhaar_no         VARCHAR(12)   NOT NULL UNIQUE,
  email_id           VARCHAR(150)  NOT NULL UNIQUE,
  mobile_no          VARCHAR(10)   NOT NULL,
  designation        VARCHAR(100)  NOT NULL,
  fathers_name       VARCHAR(100),
  dob                VARCHAR(20),
  gender             VARCHAR(10),
  pan_card_no        VARCHAR(10),
  bank_name          VARCHAR(100),
  bank_account_no    VARCHAR(20),
  ifsc_code          VARCHAR(11),
  present_address    TEXT,
  permanent_address  TEXT,
  joining_status     ENUM('INITIATED','FORM_SUBMITTED','SIGNED','APPROVED','REJECTED') NOT NULL DEFAULT 'INITIATED',
  link_status        ENUM('NOT_SENT','SENT','OPENED') NOT NULL DEFAULT 'NOT_SENT',
  onboarding_token   VARCHAR(64)   UNIQUE,
  link_sent_at       DATETIME,
  link_opened_at     DATETIME,
  approved_at        DATETIME,
  rejected_at        DATETIME,
  created_at         DATETIME      DEFAULT CURRENT_TIMESTAMP,
  updated_at         DATETIME      DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  initiated_by       VARCHAR(100),
  approved_by        VARCHAR(100),
  rejected_by        VARCHAR(100),
  rejection_reason   TEXT,
  assigned_hr_id     BIGINT,
  FOREIGN KEY (assigned_hr_id) REFERENCES hr_executives(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS audit_trail (
  id            BIGINT       PRIMARY KEY AUTO_INCREMENT,
  candidate_id  BIGINT       NOT NULL,
  action        VARCHAR(50)  NOT NULL,
  performed_by  VARCHAR(100),
  remarks       TEXT,
  performed_at  DATETIME     DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (candidate_id) REFERENCES candidates(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS candidate_documents (
  id            BIGINT       PRIMARY KEY AUTO_INCREMENT,
  candidate_id  BIGINT       NOT NULL,
  doc_type      VARCHAR(100) NOT NULL,
  file_name     VARCHAR(255),
  file_path     VARCHAR(500),
  signed        TINYINT(1)   DEFAULT 0,
  uploaded_at   DATETIME     DEFAULT CURRENT_TIMESTAMP,
  signed_at     DATETIME,
  FOREIGN KEY (candidate_id) REFERENCES candidates(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
