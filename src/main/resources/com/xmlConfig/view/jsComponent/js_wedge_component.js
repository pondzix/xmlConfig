

window.com_xmlConfig_view_jsComponent_JsWedgeComponent = function() {

  this.onStateChange = function() {
	  if(this.getState().sizeChange){
		  setGeometry(defaultGeometryDim, this.getState()) ;
	    	
	      geometryDim.nx = defaultGeometryDim.nx;
	      geometryDim.ny = defaultGeometryDim.ny;
	      geometryDim.nz = defaultGeometryDim.nz;  
	  } else {
		  var w = {wedge : {}, wedgeDim: {}, defaultWedgeDim : {}};
		  wedgeList[this.getState().wedgeId] = w;
		  calculateWedgeDimensions(this.getState(), wedgeList[this.getState().wedgeId].defaultWedgeDim);
		  setWedgeDimensions(wedgeList[this.getState().wedgeId].wedgeDim, wedgeList[this.getState().wedgeId].defaultWedgeDim)
	  }		   
  };
};

function calculateWedgeDimensions(state, wedge){
	wedge.dx = (state.dx) ? state.dx : '0';
	wedge.dy = (state.dy) ? state.dy : '0';
	wedge.dz = (state.dz) ? state.dz : '0';
	wedge.nx = (state.nx) ? state.nx : geometryDim.nx - wedge.dx;
	wedge.ny = (state.ny) ? state.ny : geometryDim.ny - wedge.dy;
	wedge.nz = (state.nz) ? state.nz : geometryDim.nz - wedge.dz;
	
	if(state.orientation == 'LowerRight'){
		wedge.A = [0,0,0];
		wedge.B = [wedge.nx, 0, 0];
		wedge.C = [wedge.nx, wedge.ny, 0];		
	} else if(state.orientation == 'LowerLeft'){
		wedge.A = [0,0,0];
		wedge.B = [wedge.nx, 0, 0];
		wedge.C = [0, wedge.ny, 0];	
		
	} else if(state.orientation == 'UpperRight'){
		wedge.A = [wedge.nx, 0, 0];
		wedge.B = [wedge.nx, wedge.ny, 0];	
		wedge.C = [0, wedge.ny, 0];		
		
	} else {
		wedge.A = [0,0,0];
		wedge.B = [wedge.nx, wedge.ny, 0];	
		wedge.C = [0, wedge.ny, 0];				
	}
	
	wedge.height = wedge.nz;

	wedge.posX = (- geometryDim.nx / 2) + (wedge.dx / 1);
	wedge.posY = (- geometryDim.ny / 2) + (wedge.dy / 1);
	wedge.posZ = (- geometryDim.nz / 2) + (wedge.dz / 1);
	
}

function setWedgeDimensions(currentDim, defaultDim){
	currentDim.nx = defaultDim.nx;
	currentDim.ny = defaultDim.ny;
	currentDim.nz = defaultDim.nz;
	currentDim.dx = defaultDim.dx;
	currentDim.dy = defaultDim.dy;
	currentDim.A = defaultDim.A;	
	currentDim.B = defaultDim.B;	
	currentDim.C = defaultDim.C;	
	currentDim.height = defaultDim.height;
	currentDim.posX = defaultDim.posX;	
	currentDim.posY = defaultDim.posY;	
	currentDim.posZ = defaultDim.posZ;	
}