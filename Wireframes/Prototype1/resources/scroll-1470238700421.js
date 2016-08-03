(function(window, undefined) {

    /*********************** START STATIC ACCESS METHODS ************************/

    jQuery.extend(jimMobile, {
        "loadScrollBars": function() {
            jQuery(".s-d12245cc-1680-458d-89dd-4f0d7fb22724 .ui-page").overscroll({ showThumbs:true, direction:'vertical' });
            jQuery(".s-fa5d63ac-330a-48af-8d6b-6f9c52fde4a0 .ui-page").overscroll({ showThumbs:true, direction:'vertical' });
            jQuery(".s-1b531444-908d-4b3d-8651-a561bf03f59e .ui-page").overscroll({ showThumbs:true, direction:'vertical' });
            jQuery(".s-1b531444-908d-4b3d-8651-a561bf03f59e #s-Category_1").overscroll({ showThumbs:false, direction:'vertical' });
            jQuery(".s-19a3d730-29a3-4409-945b-e561853ff341 .ui-page").overscroll({ showThumbs:true, direction:'vertical' });
         }
    });

    /*********************** END STATIC ACCESS METHODS ************************/

}) (window);