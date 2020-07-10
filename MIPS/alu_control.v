// alu_control.v
// Daniel Ortega [005326473] , Ben

`timescale 1ns / 1ps


module alu_control(
    input	wire	[5:0]	funct,
    input	wire	[1:0]	aluop,
    output	reg		[2:0]	select
    );
	
	
	parameter	Rtype 		=	2'b10;
	parameter	Radd		=	6'b100000;
	parameter	Rsub		=	6'b100010;
	parameter	Rand		=	6'b100100;
	parameter	Ror		=	6'b100101;
	parameter	Rslt		=	6'b101010;
	parameter	lwsw		=	2'b00;	
	parameter	Itype		=	2'b01;	
	parameter	xis		=	6'bXXXXXX;
		
	parameter	ALUadd		=	3'b010;
	parameter	ALUsub		=	3'b110;
	parameter	ALUand		=	3'b000;
	parameter	ALUor		=	3'b001;
	parameter	ALUslt		=	3'b111;
					
	parameter	unknown		=	2'b11;
	parameter	ALUx		=	3'b011;
	
	initial
		select <= 0;
	
	always@* begin
		
		if (aluop == Rtype)
		 begin
			case(funct)
				Radd:			select <= ALUadd;
				Rsub:			select <= ALUsub;
				Rand:			select <= ALUand;
				Ror:			select <= ALUor;
				Rslt:			select <= ALUslt;
				default:		select <= ALUx;
			endcase
	    end
		 
		
		else if (aluop == lwsw)	begin
			select <= ALUadd;
		end
		
		else if (aluop == Itype) begin		
			select <= ALUsub;
		end
		
		else if (aluop == unknown) begin	
			select <= ALUx;
		end
		else begin
			select <= select;			
	    end	
	end
	
endmodule

