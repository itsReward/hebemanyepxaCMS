// Initialize AOS
document.addEventListener('DOMContentLoaded', function() {
    AOS.init({
        duration: 800,
        easing: 'ease',
        once: true,
        offset: 100
    });

    // Header Scroll Effect
    window.addEventListener('scroll', function() {
        const header = document.querySelector('header');
        if (window.scrollY > 50) {
            header.classList.add('scrolled');
        } else {
            header.classList.remove('scrolled');
        }
    });

    // Mobile Menu Toggle
    const menuToggle = document.querySelector('.menu-toggle');
    const navLinks = document.querySelector('.nav-links');

    if (menuToggle) {
        menuToggle.addEventListener('click', function() {
            navLinks.classList.toggle('active');
        });
    }

    // Close menu when clicking on a link
    const navItems = document.querySelectorAll('.nav-links a');
    navItems.forEach(item => {
        item.addEventListener('click', function() {
            navLinks.classList.remove('active');
        });
    });

    // Smooth scrolling for navigation links
    document.querySelectorAll('a[href^="#"]').forEach(anchor => {
        anchor.addEventListener('click', function(e) {
            const targetId = this.getAttribute('href');

            // Only apply smooth scroll for page section links (not external links)
            if (targetId.startsWith('#') && targetId.length > 1) {
                e.preventDefault();
                const targetElement = document.querySelector(targetId);

                if (targetElement) {
                    window.scrollTo({
                        top: targetElement.offsetTop - 80,
                        behavior: 'smooth'
                    });
                }
            }
        });
    });

    // Contact Form submission
    const contactForm = document.querySelector('.contact-form form');
    if (contactForm) {
        contactForm.addEventListener('submit', function(e) {
            e.preventDefault();
            // Here you would normally handle the form submission with AJAX
            // For now, just show a confirmation message
            alert('Thank you for your message! I will get back to you soon.');
            this.reset();
        });
    }

    // Newsletter Form submission
    const newsletterForms = document.querySelectorAll('.footer-column form, .subscribe-form form');
    newsletterForms.forEach(form => {
        form.addEventListener('submit', function(e) {
            e.preventDefault();
            // Here you would normally handle the newsletter subscription
            alert('Thank you for subscribing to my newsletter!');
            this.reset();
        });
    });

    // Quote category filtering (if on quotes page)
    const categoryTabs = document.querySelectorAll('.category-tab');
    const quoteCards = document.querySelectorAll('.quote-card');

    if (categoryTabs.length > 0 && quoteCards.length > 0) {
        categoryTabs.forEach(tab => {
            tab.addEventListener('click', function() {
                // Remove active class from all tabs
                categoryTabs.forEach(t => t.classList.remove('active'));
                // Add active class to clicked tab
                this.classList.add('active');

                const category = this.getAttribute('data-category');

                // Show/hide quotes based on category
                quoteCards.forEach(card => {
                    const cardCategories = card.getAttribute('data-category');
                    if (category === 'all' || (cardCategories && cardCategories.includes(category))) {
                        card.style.display = 'flex';
                    } else {
                        card.style.display = 'none';
                    }
                });
            });
        });
    }

    // Image gallery for apparel details
    const thumbnails = document.querySelectorAll('.thumbnail');
    if (thumbnails.length > 0) {
        thumbnails.forEach(thumbnail => {
            thumbnail.addEventListener('click', function() {
                // Remove active class from all thumbnails
                thumbnails.forEach(thumb => {
                    thumb.classList.remove('active');
                });

                // Add active class to clicked thumbnail
                this.classList.add('active');

                // Update main image source
                const mainImage = document.getElementById('main-image');
                const newSrc = this.querySelector('img').getAttribute('data-src');
                if (mainImage && newSrc) {
                    mainImage.src = newSrc;
                }
            });
        });
    }
});

// Floating Stickers Script - Outlined and Bigger
document.addEventListener('DOMContentLoaded', function() {
    // Create sticker elements and insert them between sections
    const sections = document.querySelectorAll('section');

    const butterflyPngImages = [
        'assets/images/butterfly1.png',
        'assets/images/butterfly2.png'
    ];

    // Sticker designs - SVG paths for various African and literary-inspired elements
    // Now with outlines instead of fills
    const stickerDesigns = [
        // Feather pen - outlined
        `<svg viewBox="0 0 100 100" class="floating-sticker">
            <path d="M30,90 c0,0 40,-60 50,-80 c5,-10 15,-5 10,5 c-5,10 -30,50 -40,70 c-2,5 -15,10 -20,5z" 
                  fill="none" stroke="#8B5A2B" stroke-width="2"/>
            <path d="M32,85 c0,0 30,-50 40,-70 c2,-5 -5,10 -10,20 c-5,10 -20,40 -25,50 c-1,2 -3,2 -5,0z" 
                  fill="none" stroke="#CD853F" stroke-width="2"/>
        </svg>`,

        // Open book - outlined
        `<svg viewBox="0 0 100 100" class="floating-sticker">
            <path d="M10,30 c0,-10 10,-10 40,-5 l0,50 c-30,-5 -40,-5 -40,5z" 
                  fill="none" stroke="#8B5A2B" stroke-width="2"/>
            <path d="M90,30 c0,-10 -10,-10 -40,-5 l0,50 c30,-5 40,-5 40,5z" 
                  fill="none" stroke="#8B5A2B" stroke-width="2"/>
            <path d="M50,25 c-20,-2 -30,-2 -35,0 m35,50 c-20,-2 -30,-2 -35,0" 
                  stroke="#CD853F" stroke-width="2" fill="none"/>
            <path d="M50,25 c20,-2 30,-2 35,0 m-35,50 c20,-2 30,-2 35,0" 
                  stroke="#CD853F" stroke-width="2" fill="none"/>
            <line x1="50" y1="25" x2="50" y2="75" stroke="#8B5A2B" stroke-width="2" stroke-dasharray="3,3"/>
        </svg>`,

        // African mask - outlined
        `<svg viewBox="0 0 100 100" class="floating-sticker">
            <path d="M30,20 c0,0 20,-5 40,0 c5,20 5,40 0,60 c-20,5 -40,5 -40,0 c-5,-20 -5,-40 0,-60z" 
                  fill="none" stroke="#8B5A2B" stroke-width="2"/>
            <path d="M40,30 a5,10 0 0 1 20,0 a5,10 0 0 1 -20,0z" 
                  fill="none" stroke="#CD853F" stroke-width="1.5"/>
            <path d="M35,50 a2,4 0 0 0 10,0 a2,4 0 0 0 -10,0z M55,50 a2,4 0 0 0 10,0 a2,4 0 0 0 -10,0z" 
                  fill="none" stroke="#CD853F" stroke-width="1.5"/>
            <path d="M40,70 a10,5 0 0 0 20,0z" 
                  fill="none" stroke="#CD853F" stroke-width="1.5"/>
            <path d="M30,35 c0,0 10,0 10,10 M70,35 c0,0 -10,0 -10,10" 
                  stroke="#8B5A2B" stroke-width="1.5" fill="none"/>
        </svg>`,

        // Baobab tree - outlined
        `<svg viewBox="0 0 100 100" class="floating-sticker">
            <path d="M50,20 c-10,0 -20,10 -20,20 c0,10 5,15 5,30 l30,0 c0,-15 5,-20 5,-30 c0,-10 -10,-20 -20,-20z" 
                  fill="none" stroke="#8B5A2B" stroke-width="2"/>
            <path d="M40,70 l0,20 l20,0 l0,-20z" 
                  fill="none" stroke="#8B5A2B" stroke-width="2"/>
            <path d="M30,40 c0,0 -10,-10 -15,-5 c-5,5 5,10 5,10z M70,40 c0,0 10,-10 15,-5 c5,5 -5,10 -5,10z" 
                  fill="none" stroke="#CD853F" stroke-width="1.5"/>
            <path d="M35,30 c0,0 -10,-5 -10,0 c0,5 5,5 5,5z M65,30 c0,0 10,-5 10,0 c0,5 -5,5 -5,5z" 
                  fill="none" stroke="#CD853F" stroke-width="1.5"/>
        </svg>`,

        // African pattern element - already outlined
        `<svg viewBox="0 0 100 100" class="floating-sticker">
            <path d="M20,20 L80,20 L80,80 L20,80 Z" fill="none" stroke="#8B5A2B" stroke-width="2"/>
            <path d="M35,35 L65,35 L65,65 L35,65 Z" fill="none" stroke="#CD853F" stroke-width="2"/>
            <path d="M20,20 L35,35 M80,20 L65,35 M20,80 L35,65 M80,80 L65,65" stroke="#D2B48C" stroke-width="2"/>
            <circle cx="50" cy="50" r="5" fill="none" stroke="#8B5A2B" stroke-width="2"/>
        </svg>`,

        // Adinkra symbol - new outlined sticker
        `<svg viewBox="0 0 100 100" class="floating-sticker">
            <circle cx="50" cy="50" r="35" fill="none" stroke="#8B5A2B" stroke-width="2"/>
            <path d="M50,25 L65,50 L50,75 L35,50 Z" fill="none" stroke="#CD853F" stroke-width="2"/>
            <path d="M25,50 L50,50 M75,50 L50,50 M50,25 L50,75" stroke="#8B5A2B" stroke-width="2" stroke-dasharray="5,3"/>
            <circle cx="50" cy="50" r="5" fill="none" stroke="#CD853F" stroke-width="1.5"/>
        </svg>`,

        // Quill and ink - new outlined sticker
        `<svg viewBox="0 0 100 100" class="floating-sticker">
            <path d="M30,80 c0,0 10,-30 20,-50 c5,-10 15,-5 10,0 c-5,5 -20,30 -20,45 c0,5 -5,10 -10,5z" 
                  fill="none" stroke="#8B5A2B" stroke-width="2"/>
            <path d="M65,75 c0,0 10,5 10,-5 c0,-10 -10,-15 -10,-5 c0,10 10,15 10,5" 
                  fill="none" stroke="#CD853F" stroke-width="2"/>
            <ellipse cx="65" cy="80" rx="15" ry="5" fill="none" stroke="#8B5A2B" stroke-width="2"/>
        </svg>`
    ];

    // Insert stickers between sections
    for (let i = 0; i < sections.length - 1; i++) {
        const stickerContainer = document.createElement('div');
        stickerContainer.className = 'sticker-container';
        const randomIndex = Math.floor(Math.random() * stickerDesigns.length);
        stickerContainer.innerHTML = stickerDesigns[randomIndex];

        // Randomize position within the container slightly
        const sticker = stickerContainer.querySelector('.floating-sticker');
        const randomX = Math.floor(Math.random() * 30) - 15; // -15 to 15
        sticker.style.transform = `translateX(${randomX}%)`;

        // Insert after current section
        sections[i].after(stickerContainer);
    }

    // Add floating animation to all stickers
    const stickers = document.querySelectorAll('.floating-sticker');
    stickers.forEach((sticker, index) => {
        // Different animation delay for each sticker
        const delay = index * 0.5;
        sticker.style.animationDelay = `${delay}s`;
    });
});