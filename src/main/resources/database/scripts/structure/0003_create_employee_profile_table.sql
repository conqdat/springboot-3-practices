CREATE TABLE "employee_profile" (
    "id" SERIAL NOT NULL,
    "employee_id" integer NOT NULL,
    "go_fluent" VARCHAR(10) NOT NULL,
    "description" varchar(250) NULL,
    "created_by" varchar(50) NOT NULL,
    "created_date" timestamp NOT NULL,
    "updated_by" varchar(50) NULL,
    "updated_date" timestamp NULL
)
;

ALTER TABLE "employee_profile" ADD CONSTRAINT "PK_employee_profile"
	PRIMARY KEY ("id")
;

CREATE INDEX "IXFK_employee_profile_employee" ON "employee_profile" ("employee_id" ASC)
;

ALTER TABLE "employee_profile" ADD CONSTRAINT "FK_employee_profile_employee"
	FOREIGN KEY ("employee_id") REFERENCES "employee" ("id") ON DELETE Cascade ON UPDATE Cascade
;

ALTER TABLE "employee_status"
	ADD COLUMN "description" varchar(250) NULL;

