PROGRAM MAKEDNA;

(* PROGRAM TO COMPUTE DOUBLE-HELIX *)
(* ANDREW PICKHOLTZ - JAN 1981 *)

CONST
        (* DESCRIPTION OF DOUBLE-HELIX *)
        (* SEE J. D. WATSON, MOLECULAR BIOLOGY OF THE GENE *)
        (* SECOND EDITION, PAGES 261-262 *)
        (* ALL UNITS IN ANGSTROMS *)
  RADIUS = 10;
  HTURN = 34;     (* HEIGHT OF ONE TURN *)
  NNPT = 10;      (* NUMBER OF NUCLEOTIDES PER TURN *)
  OFFSET = 2.402; (* OFFSET FOR SECOND HELIX IN RADIANS *)
  PI = 3.1415926535897932384626;
  CMPA = 0.1569;  (* CM PER ANGSTROM FOR MAGNIFICATION *)
  NNUCL = 15;     (* HALF NUMBER OF NUCLEOTIDES TO DISPLAY *)

VAR
  NUCL: -NNUCL .. NNUCL;
  I, J: INTEGER;
  DISK: FILE OF REAL;

PROCEDURE DUMP(DATA: REAL);

BEGIN
  write(DISK, DATA)
END;

BEGIN
  WRITELN;
  WRITELN('WRITING');
  assign(DISK,'DOUBLE-HELIX');
  rewrite(disk);
  (* NUMBER OF POINTS *)
  DUMP(2 * (2 * NNUCL + 1));
  FOR NUCL := -NNUCL TO NNUCL DO
  BEGIN
    (* FIRST HELIX *)
    DUMP(CMPA * RADIUS * COS(NUCL * 2 * PI / NNPT));
    DUMP(CMPA * RADIUS * SIN(NUCL * 2 * PI / NNPT));
    DUMP(CMPA * HTURN / NNPT * NUCL);
    (* SECOND HELIX *)
    DUMP(CMPA * RADIUS * COS(NUCL * 2 * PI / NNPT + OFFSET));
    DUMP(CMPA * RADIUS * SIN(NUCL * 2 * PI / NNPT + OFFSET));
    DUMP(CMPA * HTURN / NNPT * NUCL)
  END;
  (* NUMBER OF EDGES *)
  DUMP(8 * NNUCL);
  FOR J := 1 TO 2 * NNUCL DO
  BEGIN
    I := 2 * J - 1;
    DUMP(-I);
    DUMP(I + 2);
    DUMP(I + 3);
    DUMP(I + 1)
  END;
  CLOSE(DISK);
  WRITELN('DONE...')
END.

