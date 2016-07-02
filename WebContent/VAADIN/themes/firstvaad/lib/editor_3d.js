
			var container;
			var camera, controls, scene, renderer;
			var objects = [], plane;
			var geom;

			var raycaster = new THREE.Raycaster();
			var mouse = new THREE.Vector2(),
			offset = new THREE.Vector3(),
			INTERSECTED, SELECTED;
			
			
			//WEDGE
			PrismGeometry = function ( vertices, height ) {

				var Shape = new THREE.Shape();

				( function f( ctx ) {

					ctx.moveTo( vertices[0].x, vertices[0].y );
					for (var i=1; i < vertices.length; i++) {
						ctx.lineTo( vertices[i].x, vertices[i].y );
					}
					ctx.lineTo( vertices[0].x, vertices[0].y );

				} )( Shape );

				var settings = { };
				settings.amount = height;
				settings.bevelEnabled = false;
				THREE.ExtrudeGeometry.call( this, Shape, settings );

};

			PrismGeometry.prototype = Object.create( THREE.ExtrudeGeometry.prototype );
			
		
			controls = { enabled: true };
			function init() {

				container = $("#container")[0];
				camera = new THREE.PerspectiveCamera( 70, window.innerWidth / window.innerHeight, 1, 10000 );
				camera.position.z = 1000;

				controls = new THREE.TrackballControls( camera );
				controls.rotateSpeed = 3.0;
				controls.zoomSpeed = 1.2;
				controls.panSpeed = 0.8;
				controls.noZoom = false;
				controls.noPan = false;
				controls.staticMoving = true;
				controls.dynamicDampingFactor = 0.3;

				scene = new THREE.Scene();

				scene.add( new THREE.AmbientLight( 0x505050 ) );

				var light = new THREE.SpotLight( 0xffffff, 1.5 );
				light.position.set( 0, 500, 2000 );
				light.castShadow = true;

				light.shadowCameraNear = 200;
				light.shadowCameraFar = camera.far;
				light.shadowCameraFov = 50;

				light.shadowBias = -0.00022;
				light.shadowDarkness = 0.5;

				light.shadowMapWidth = 2048;
				light.shadowMapHeight = 2048;
		
				scene.add(light);
				if(parent.window.defaultGeometryDim &&
				   parent.window.defaultGeometryDim && 
				   parent.window.defaultGeometryDim){
					geom = new THREE.BoxGeometry(parent.window.defaultGeometryDim.nx,
						     parent.window.defaultGeometryDim.ny,
							 parent.window.defaultGeometryDim.nz);		
				} else{
					geom = new THREE.BoxGeometry(0, 0, 0);
				}
				
			/*	;*/

				

				parent.window.geometry = new THREE.Mesh( geom, new THREE.MeshLambertMaterial( { color: 0xFF0000, wireframe: true} ) );
				
				
				parent.window.geometry.position.x = 0;
				parent.window.geometry.position.y = 0;
				parent.window.geometry.position.z = 0;

				parent.window.geometry.rotation.x = 0;
				parent.window.geometry.rotation.y = 0;
				parent.window.geometry.rotation.z = 0;


				//parent.window.box.castShadow = true;
				//parent.window.box.receiveShadow = true;

				scene.add(parent.window.geometry);
				//scene.add(parent.window.box);
				if(window.parent.boxList){
					addBoxes(scene, objects);
				}
				if(window.parent.wedgeList){
					addWedges(scene, objects);
				}
				
				scene.add( new THREE.AxisHelper(100));

				objects.push(parent.window.geometry);
				//objects.push(parent.window.box);

				plane = new THREE.Mesh(
					new THREE.PlaneBufferGeometry( 2000, 2000, 8, 8 ),
					new THREE.MeshBasicMaterial( { color: 0x000000, opacity: 0.25, transparent: true } )
				);
				plane.visible = false;
				scene.add( plane );

				renderer = new THREE.WebGLRenderer( { antialias: true, alpha : true} );
				//renderer.setClearColor( 0x000000 );
				//renderer.setPixelRatio( window.devicePixelRatio );
				renderer.setSize( window.innerWidth, window.innerHeight );
				renderer.sortObjects = true;

				//renderer.shadowMapEnabled = true;
				//renderer.shadowMapType = THREE.PCFShadowMap;

				container.appendChild( renderer.domElement );

				renderer.domElement.addEventListener( 'mousemove', onDocumentMouseMove, false );
				renderer.domElement.addEventListener( 'mousedown', onDocumentMouseDown, false );
				renderer.domElement.addEventListener( 'mouseup', onDocumentMouseUp, false );
				window.addEventListener( 'resize', onWindowResize, false );
			}

			function onWindowResize() {
				camera.aspect = window.innerWidth / window.innerHeight;
				camera.updateProjectionMatrix();
				renderer.setSize( window.innerWidth, window.innerHeight );
			}

			function onDocumentMouseMove( event ) {
				event.preventDefault();
				mouse.x = ( event.clientX / window.innerWidth ) * 2 - 1;
				mouse.y = - ( event.clientY / window.innerHeight ) * 2 + 1;
				raycaster.setFromCamera( mouse, camera );
				if ( SELECTED ) {
					var intersects = raycaster.intersectObject( plane );
					SELECTED.position.copy( intersects[ 0 ].point.sub( offset ) );
					return;
				}

				var intersects = raycaster.intersectObjects( objects );
				if ( intersects.length > 0 ) {
					if ( INTERSECTED != intersects[ 0 ].object ) {
						if ( INTERSECTED ) INTERSECTED.material.color.setHex( INTERSECTED.currentHex );
						INTERSECTED = intersects[ 0 ].object;
						INTERSECTED.currentHex = INTERSECTED.material.color.getHex();
						INTERSECTED.material.color.setHex( 255 );
						plane.position.copy( INTERSECTED.position );
						plane.lookAt( camera.position );
					}
					container.style.cursor = 'pointer';
				} else {
					if ( INTERSECTED ) INTERSECTED.material.color.setHex( INTERSECTED.currentHex );
					INTERSECTED = null;
					container.style.cursor = 'auto';
				}
			}

			function onDocumentMouseDown( event ) {
				event.preventDefault();
				var vector = new THREE.Vector3( mouse.x, mouse.y, 0.5 ).unproject( camera );
			raycaster.setFromCamera( mouse, camera );
//			var raycaster = new THREE.Raycaster( camera.position, vector.sub( camera.position ).normalize() );
				var intersects = raycaster.intersectObjects( objects );
				if ( intersects.length > 0 ) {
					controls.enabled = false;
					SELECTED = intersects[ 0 ].object;
					var intersects = raycaster.intersectObject( plane );
					offset.copy( intersects[ 0 ].point ).sub( plane.position );
					container.style.cursor = 'move';
				}
			}

			function stopControl( event ) {
					controls.enabled = false;
			}

			function startControl( event ) {
					controls.enabled = true;
			}

			function onDocumentMouseUp( event ) {
				event.preventDefault();
				controls.enabled = true;
				if ( INTERSECTED ) {
					plane.position.copy( INTERSECTED.position );
					SELECTED = null;
				}
				container.style.cursor = 'auto';
			}

			function animate() {
				requestAnimationFrame( animate );
				render();
			}

			function render() {
				controls.update();
				renderer.render( scene, camera );
			}
			
			function addBoxes(scene, objects){					
				for(var b in window.parent.boxList){
					var boxItem = window.parent.boxList[b];
					var boxGeom = new THREE.BoxGeometry(boxItem.defaultBoxDim.nx,
							boxItem.defaultBoxDim.ny,
							boxItem.defaultBoxDim.nz);
					
					boxItem.box = new THREE.Mesh( boxGeom, new THREE.MeshLambertMaterial( { color:  Math.random() * 0xffffff, opacity: 0.5, transparent: true} ) );
					boxItem.box.position.x = boxItem.defaultBoxDim.posX;
					boxItem.box.position.y = boxItem.defaultBoxDim.posY;
					boxItem.box.position.z = boxItem.defaultBoxDim.posZ;
					scene.add(boxItem.box);
					objects.push(boxItem.box);
				}			
			}
			
			function addWedges(scene, objects){					
				for(var w in window.parent.wedgeList){
					var wedgeItem = window.parent.wedgeList[w];
					var A = new THREE.Vector2(wedgeItem.wedgeDim.A[0], wedgeItem.wedgeDim.A[1], wedgeItem.wedgeDim.A[2]);
					var B = new THREE.Vector2(wedgeItem.wedgeDim.B[0], wedgeItem.wedgeDim.B[1], wedgeItem.wedgeDim.B[2]);
					var C = new THREE.Vector2(wedgeItem.wedgeDim.C[0], wedgeItem.wedgeDim.C[1], wedgeItem.wedgeDim.C[2]);
					var height = wedgeItem.wedgeDim.height;
					var wedgeGeom = new PrismGeometry( [ A, B, C ], height ); 
					var material = new THREE.MeshPhongMaterial( { color: 0x00b2fc, specular: 0x00ffff, shininess: 20, opacity: 0.5, transparent: true} );
					wedgeItem.wedge = new THREE.Mesh( wedgeGeom, material);
					
					
					wedgeItem.wedge.position.x = wedgeItem.defaultWedgeDim.posX;
					wedgeItem.wedge.position.y = wedgeItem.defaultWedgeDim.posY;
					wedgeItem.wedge.position.z = wedgeItem.defaultWedgeDim.posZ;

					scene.add(wedgeItem.wedge);
					objects.push(wedgeItem.wedge);
				}			
			}
			
