// execute.v
// Daniel Ortega [005326473] , Ben

`timescale 1ns / 1ps

module EXECUTE(
	input		wire	[1:0]		wb_ctl,
	input		wire	[2:0]		m_ctl,
	input		wire				regdst,
	input		wire				alusrc,
	input		wire	[1:0]		aluop, 
	input		wire	[31:0]		npcout,
	input		wire	[31:0]		rdata1, 
	input		wire	[31:0]		rdata2, 
	input		wire	[31:0]		s_extendout,
	input		wire	[4:0]		instrout_2016,
	input		wire	[4:0]		instrout_1511,
	output		wire	[1:0]		wb_ctlout,
	output		wire				branch,
	output		wire				memread,
	output		wire				memwrite,
	output		wire	[31:0]		EX_MEM_NPC,
	output		wire				zero,
	output		wire	[31:0]		alu_result,
	output		wire	[31:0]		rdata2out,
	output		wire	[4:0]		five_bit_muxout
	);

	
	wire	[31:0]		adder_out;
	wire	[31:0]		b;
	wire	[31:0]		aluout;
	wire	[4:0]		muxout;
	wire	[2:0]		control;
	wire				aluzero;
	
	
	mux  
	#(.BITS(5)) mux_inst (
	.y(muxout),		// Output of Multiplexer
	.a(instrout_1511),	// Input 1 of Multiplexer
	.b(instrout_2016),	// Input 0 of Multiplexer
	.sel(regdst)	// Select Input
   );
	
	mux  
	#(.BITS(32)) mux_ALUin (
	.y(b),		// Output of Multiplexer
	.a(s_extendout),	// Input 1 of Multiplexer
	.b(rdata2),		// Input 0 of Multiplexer
	.sel(alusrc)	// Select Input
   );	
   
   adder adder_inst(
    .add_in1(npcout),
    .add_in2(s_extendout),
    .add_out(adder_out)
    );
	
ex_mem ex_mem_inst(
	.ctlwb_out(wb_ctl),	   //	input	wire	[1:0]	
	.ctlm_out(m_ctl),          //	input	wire	[2:0]	
	.adder_out(adder_out),     //	input	wire	[31:0]	
	.aluzero(aluzero),         //	input	wire			
	.readdat2(rdata2),         //	input	wire	[31:0]	
	.aluout(aluout),      	   //	input	wire	[31:0]	
	.muxout(muxout),           //	input	wire	[4:0]	
	.wb_ctlout(wb_ctlout),     //	output	reg	[1:0]	
	.branch(branch),           //	output	reg				
	.memread(memread),         //	output	reg				
	.memwrite(memwrite),       //	output	reg				
	.add_result(EX_MEM_NPC),   //	output	reg	[31:0]	
	.zero(zero),          	   //	output	reg				
	.alu_result(alu_result),   //	output	reg	[31:0]	
	.rdata2out(rdata2out),     //	output	reg	[31:0]	
	.five_bit_muxout(five_bit_muxout)    //	output	reg		[4:0]	
    );
	
	alu_control alu_control_inst (
    .funct(s_extendout[5:0]),		//	input	wire	[5:0]
    .aluop(aluop),     	//	input	wire	[1:0]
    .select(control)    //	output	reg	[2:0]
    );
	
	alu alu_inst(
    .a(rdata1),				//	input	wire	[31:0]	
    .b(b),           	//	input	wire	[31:0]	
    .control(control),  //	input	wire	[2:0]	
    .result(aluout),	//	output	reg	[31:0]	
    .zero(aluzero)  	//	output	wire			
    );
endmodule 
