CREATE TABLE "employee_on_bench" (
    "id" SERIAL NOT NULL,
    "name" varchar(250) NOT NULL,
    "start_date" timestamp NOT NULL,
	"end_date" timestamp NOT NULL,
    "created_by" varchar(50) NOT NULL,
    "created_date" timestamp NOT NULL,
    "updated_by" varchar(50) NULL,
    "updated_date" timestamp NULL
)
;

CREATE TABLE "employee_on_bench_detail" (
    "id" BIGSERIAL NOT NULL,
    "employee_id" integer NOT NULL,
    "employee_on_bench_id" integer NOT NULL,
	"bench_days" integer NULL,
	"date_of_join" timestamp NULL,
	"status_change_date" timestamp NULL,
	"category_code" integer NULL,
    "created_by" varchar(50) NOT NULL,
    "created_date" timestamp NOT NULL,
    "updated_by" varchar(50) NULL,
    "updated_date" timestamp NULL
)
;

CREATE TABLE "employee_import" (
    "id" serial NOT NULL,
    "name" varchar(255) NOT NULL,
    "type" integer NOT NULL,
    "status" integer NOT NULL,
    "message" jsonb NULL,
    "created_by" varchar(50) NOT NULL,
    "created_date" timestamp NOT NULL,
    "updated_by" varchar(50) NULL,
    "updated_date" timestamp NULL
)
;

CREATE TABLE "employee_import_detail" (
    "id" bigserial NOT NULL,
    "body" jsonb NULL,
    "status" integer NOT NULL,
    "line_num" integer NULL,
    "message_line_list" jsonb NULL,
    "employee_import_id" integer NOT NULL,
    "created_by" varchar(50) NOT NULL,
    "created_date" timestamp NOT NULL,
    "updated_by" varchar(50) NULL,
    "updated_date" timestamp NULL
)
;

-- PK
ALTER TABLE "employee_on_bench" ADD CONSTRAINT "PK_employee_on_bench"
	PRIMARY KEY ("id")
;

ALTER TABLE "employee_on_bench_detail" ADD CONSTRAINT "PK_employee_on_bench_detail"
	PRIMARY KEY ("id")
;

ALTER TABLE "employee_import" ADD CONSTRAINT "PK_employee_import" 
    PRIMARY KEY ("id")
;

ALTER TABLE "employee_import_detail" ADD CONSTRAINT "PK_employee_import_detail"
    PRIMARY KEY ("id")
;

-- FK
ALTER TABLE "employee_on_bench_detail" ADD CONSTRAINT "FK_employee_on_bench_detail_employee"
	FOREIGN KEY ("employee_id") REFERENCES "employee" ("id") ON DELETE CASCADE ON UPDATE CASCADE
;

ALTER TABLE "employee_on_bench_detail" ADD CONSTRAINT "FK_employee_on_bench_detail_employee_on_bench"
	FOREIGN KEY ("employee_on_bench_id") REFERENCES "employee_on_bench" ("id") ON DELETE CASCADE ON UPDATE CASCADE
;

ALTER TABLE "employee_import_detail" ADD CONSTRAINT "FK_employee_import_detail_employee_import" 
    FOREIGN KEY ("employee_import_id") REFERENCES "employee_import" ("id") ON DELETE CASCADE ON UPDATE CASCADE
;

-- index
CREATE INDEX "IXFK_employee_on_bench_detail_employee" ON "employee_on_bench_detail" ("employee_id" ASC)
;

CREATE INDEX "IXFK_employee_on_bench_detail_employee_on_bench" ON "employee_on_bench_detail" ("employee_on_bench_id" ASC)
;

CREATE INDEX "IXFK_employee_import_detail_employee_import_id" ON "employee_import_detail" ("employee_import_id" ASC)
;