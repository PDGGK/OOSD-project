// ==================== Internationalization Data ==================== 
const translations = {
    en: {
        "page.title": "Shadow Donkey Kong - Development Journey",
        "nav.overview": "Overview",
        "nav.journey": "Journey", 
        "nav.challenges": "Challenges",
        "nav.architecture": "Architecture",
        "nav.techStack": "Tech Stack",
        "nav.achievements": "Achievements",
        "nav.lessons": "Lessons",
        "hero.badge": "SWEN20003 Project 2B",
        "hero.subtitle": "Development Journey",
        "hero.description": "A comprehensive documentation of building a classic platformer game using Java and object-oriented design principles. From initial architecture to final optimization - every bug, every refactor, every breakthrough.",
        "hero.stats.classes": "Java Classes",
        "hero.stats.lines": "Lines of Code",
        "hero.stats.commits": "Git Commits",
        "hero.stats.functionality": "Functionality",
        "hero.actions.explore": "Explore Journey",
        "hero.actions.code": "View Code",
        "overview.title": "Project Overview",
        "overview.subtitle": "Building a sophisticated 2D platformer with advanced game mechanics",
        "overview.cards.architecture.title": "Multi-Level Architecture",
        "overview.cards.architecture.desc": "Implemented a scalable level system with Level1Screen and Level2Screen, supporting different game mechanics and entities per level.",
        "overview.cards.entities.title": "Complex Entity System",
        "overview.cards.entities.desc": "Created 15+ entity types including Mario, Donkey Kong, Barrels, Monkeys, Projectiles with sophisticated inheritance hierarchies.",
        "overview.cards.physics.title": "Physics Engine",
        "overview.cards.physics.desc": "Built custom physics with gravity, collision detection, platform alignment, and projectile movement systems.",
        "overview.cards.ai.title": "AI Behavior",
        "overview.cards.ai.desc": "Implemented intelligent monkey patrolling, banana shooting mechanics, and complex collision interactions.",
        "techStack.title": "Technology Stack",
        "techStack.subtitle": "Tools and technologies powering the development",
        "techStack.categories.core.title": "Core Technologies",
        "techStack.categories.design.title": "Design Patterns", 
        "techStack.categories.tools.title": "Development Tools",
        "journey.title": "Development Journey",
        "journey.subtitle": "A chronological walkthrough of the entire development process",
        "journey.phases.phase1.title": "Initial Architecture Design",
        "journey.phases.phase1.desc": "Established inheritance hierarchies, created abstract base classes, and designed the overall system architecture following OOP principles.",
        "journey.phases.phase2.title": "Critical Bug Discovery: Cross-Layer Jump Scoring",
        "journey.phases.phase2.desc": "Discovered and fixed a severe bug where Mario jumping on any layer could trigger scoring for barrels on different layers. Implemented vertical distance validation.",
        "journey.phases.phase3.title": "Mario Air Control Enhancement",
        "journey.phases.phase3.desc": "Enhanced Mario's movement system to maintain horizontal velocity in mid-air after leaving ladders, providing better player control and more natural game feel.",
        "journey.phases.phase4.title": "UI System Refactoring",
        "journey.phases.phase4.desc": "Eliminated hardcoded UI positions and implemented dynamic layout calculations based on app.properties configuration for better maintainability.",
        "journey.phases.phase5.title": "Code Quality Optimization",
        "journey.phases.phase5.desc": "Removed deprecated PLAYING state, eliminated unused ProjectileInterface, and cleaned up redundant code for production-quality standards.",
        "challenges.title": "Technical Challenges",
        "challenges.subtitle": "Deep dives into the most complex problems and their solutions",
        "challenges.difficulty.high": "High Complexity",
        "challenges.difficulty.medium": "Medium Complexity",
        "challenges.sections.problem": "Problem",
        "challenges.sections.solution": "Solution",
        "challenges.sections.impact": "Impact",
        "challenges.cards.collision.title": "Cross-Layer Collision Detection",
        "challenges.cards.collision.problem": "Mario jumping on the top layer could incorrectly trigger scoring for barrels on lower layers, leading to unfair point accumulation and breaking game balance.",
        "challenges.cards.collision.solution": "Implemented vertical distance validation with 80-pixel maximum threshold and enhanced the collision detection system with multiple validation layers and Z-axis considerations.",
        "challenges.cards.collision.impact": "Eliminated false positive scoring, ensuring fair gameplay and maintaining game balance integrity across all levels.",
        "challenges.cards.physics.title": "Mario Movement Physics",
        "challenges.cards.physics.problem": "Mario would lose horizontal momentum immediately upon leaving ladders, creating unnatural movement and poor game feel that frustrated players.",
        "challenges.cards.physics.solution": "Modified velocity reset logic to preserve horizontal velocity in mid-air, allowing natural air control while maintaining physics accuracy and realistic movement.",
        "challenges.cards.physics.impact": "Significantly improved player experience and control responsiveness, making the game feel more professional and enjoyable.",
        "challenges.cards.architecture.title": "Scalable Architecture Design",
        "challenges.cards.architecture.problem": "Code duplication between level implementations and hardcoded values scattered throughout the codebase, making maintenance difficult and error-prone.",
        "challenges.cards.architecture.solution": "Created generic utility methods in GameplayScreen base class and centralized configuration management through properties files and template method pattern.",
        "challenges.cards.architecture.impact": "Reduced code duplication by 40% and improved maintainability significantly, enabling faster feature development and easier debugging.",
        "challenges.metrics.bugs": "False Positives",
        "challenges.metrics.accuracy": "Accuracy",
        "challenges.metrics.improvement": "Control Improvement",
        "challenges.metrics.response": "Response Time",
        "challenges.metrics.reduction": "Code Reduction",
        "challenges.metrics.patterns": "Design Patterns",
        "architecture.title": "System Architecture",
        "architecture.subtitle": "Comprehensive overview of the object-oriented design",
        "architecture.hierarchy.title": "Class Hierarchy",
        "architecture.principles.title": "Design Principles Applied",
        "architecture.principles.inheritance": "Proper use of abstract classes and concrete implementations for code reuse and logical hierarchy",
        "architecture.principles.encapsulation": "Private attributes with controlled access through getters/setters maintaining data integrity",
        "architecture.principles.polymorphism": "Interface-based design for flexible entity interactions and runtime behavior selection",
        "architecture.principles.composition": "Modular components for complex behavior assembly and loose coupling",
        "architecture.stats.classes": "Classes",
        "architecture.stats.abstracts": "Abstract Classes",
        "architecture.stats.interfaces": "Interfaces",
        "architecture.stats.levels": "Inheritance Levels",
        "achievements.title": "Project Achievements",
        "achievements.subtitle": "Measurable accomplishments and milestones reached",
        "achievements.cards.score.title": "Perfect Functionality Score",
        "achievements.cards.score.desc": "Achieved full marks on game functionality implementation with all requirements met",
        "achievements.cards.bugs.title": "Zero Critical Bugs",
        "achievements.cards.bugs.desc": "Delivered production-ready code with comprehensive testing and edge case handling",
        "achievements.cards.commits.title": "Meaningful Git Commits",
        "achievements.cards.commits.desc": "Maintained excellent version control practices with descriptive commit messages",
        "achievements.cards.delivery.title": "On-Time Delivery",
        "achievements.cards.delivery.desc": "Completed all project phases within deadlines while maintaining quality standards",
        "achievements.cards.performance.title": "Performance Optimization",
        "achievements.cards.performance.desc": "Optimized game loop and rendering for smooth 60fps gameplay experience",
        "achievements.cards.documentation.title": "Complete Documentation",
        "achievements.cards.documentation.desc": "Comprehensive Javadoc comments and README documentation for all components",
        "lessons.title": "Key Takeaways",
        "lessons.subtitle": "Valuable insights gained throughout the development process",
        "lessons.cards.testing.title": "Thorough Testing is Crucial",
        "lessons.cards.testing.desc": "The cross-layer jump bug was discovered through edge case testing. Always test boundary conditions and unexpected user behavior scenarios to ensure robust software.",
        "lessons.cards.testing.highlight": '"Edge cases reveal the true quality of your code"',
        "lessons.cards.clean.title": "Clean Code Pays Off",
        "lessons.cards.clean.desc": "Removing deprecated code and unused interfaces improved maintainability and prevented future confusion. Clean, well-documented code is professional code that stands the test of time.",
        "lessons.cards.clean.highlight": '"Code is read more often than it\'s written"',
        "lessons.cards.ux.title": "User Experience Matters",
        "lessons.cards.ux.desc": "Small improvements in Mario's air control significantly enhanced the overall game feel and player satisfaction. Never underestimate the impact of polished interactions.",
        "lessons.cards.ux.highlight": '"Great UX is invisible until it\'s missing"',
        "lessons.cards.architecture.title": "Architecture Decisions Impact Everything",
        "lessons.cards.architecture.desc": "Well-designed inheritance hierarchies and interfaces made adding new features and fixing bugs much easier. Good architecture is an investment in future development speed.",
        "lessons.cards.architecture.highlight": '"Good architecture enables, bad architecture constrains"',
        "footer.description": "A complete journey from concept to implementation",
        "footer.duration": "3 weeks development",
        "footer.grade": "Grade: HD",
        "footer.sections.project": "Project",
        "footer.sections.documentation": "Documentation",
        "footer.links.source": "Source Code",
        "footer.links.download": "Download Game",
        "footer.links.readme": "README",
        "footer.links.javadoc": "Javadoc",
        "footer.copyright": "© 2024 SWEN20003 Project 2B Development Journal",
        "footer.tech": "Made with"
    },
    zh: {
        "page.title": "Shadow Donkey Kong - 开发历程",
        "nav.overview": "项目概览",
        "nav.journey": "开发历程",
        "nav.challenges": "技术挑战",
        "nav.architecture": "系统架构",
        "nav.techStack": "技术栈",
        "nav.achievements": "项目成就",
        "nav.lessons": "经验总结",
        "hero.badge": "SWEN20003 Project 2B",
        "hero.subtitle": "开发历程",
        "hero.description": "使用Java和面向对象设计原则构建经典平台跳跃游戏的完整文档记录。从初始架构到最终优化 - 每一个bug、每一次重构、每一个突破。",
        "hero.stats.classes": "Java类",
        "hero.stats.lines": "代码行数",
        "hero.stats.commits": "Git提交",
        "hero.stats.functionality": "功能完整度",
        "hero.actions.explore": "探索历程",
        "hero.actions.code": "查看代码",
        "overview.title": "项目概览", 
        "overview.subtitle": "构建具有高级游戏机制的复杂2D平台游戏",
        "overview.cards.architecture.title": "多关卡架构",
        "overview.cards.architecture.desc": "实现了可扩展的关卡系统，包含Level1Screen和Level2Screen，每个关卡支持不同的游戏机制和实体。",
        "overview.cards.entities.title": "复杂实体系统",
        "overview.cards.entities.desc": "创建了15+种实体类型，包括Mario、Donkey Kong、Barrel、Monkey、Projectile等，具有精密的继承层次结构。",
        "overview.cards.physics.title": "物理引擎",
        "overview.cards.physics.desc": "构建了包含重力、碰撞检测、平台对齐和弹射物移动系统的自定义物理引擎。",
        "overview.cards.ai.title": "AI行为",
        "overview.cards.ai.desc": "实现了智能猴子巡逻、香蕉射击机制和复杂的碰撞交互系统。",
        "techStack.title": "技术栈",
        "techStack.subtitle": "支撑开发的工具和技术",
        "techStack.categories.core.title": "核心技术",
        "techStack.categories.design.title": "Design Pattern",
        "techStack.categories.tools.title": "开发工具",
        "journey.title": "开发历程",
        "journey.subtitle": "整个开发过程的时间线记录",
        "journey.phases.phase1.title": "初始架构设计",
        "journey.phases.phase1.desc": "建立继承层次结构，创建抽象基类，并遵循OOP原则设计整体系统架构。",
        "journey.phases.phase2.title": "关键Bug发现：跨层跳跃计分",
        "journey.phases.phase2.desc": "发现并修复了一个严重bug：Mario在任意层跳跃都可能触发不同层barrel的计分。实现了垂直距离验证。",
        "journey.phases.phase3.title": "Mario空中控制增强",
        "journey.phases.phase3.desc": "增强了Mario的移动系统，在离开梯子后保持水平速度，提供更好的玩家控制和更自然的游戏感受。",
        "journey.phases.phase4.title": "UI系统重构",
        "journey.phases.phase4.desc": "消除了硬编码的UI位置，基于app.properties配置实现动态布局计算，提高可维护性。",
        "journey.phases.phase5.title": "代码质量优化",
        "journey.phases.phase5.desc": "移除了弃用的PLAYING状态，删除了未使用的ProjectileInterface，清理冗余代码以达到生产质量标准。",
        "challenges.title": "技术挑战",
        "challenges.subtitle": "最复杂问题及其解决方案的深入分析",
        "challenges.difficulty.high": "高复杂度",
        "challenges.difficulty.medium": "中等复杂度", 
        "challenges.sections.problem": "问题",
        "challenges.sections.solution": "解决方案",
        "challenges.sections.impact": "影响",
        "challenges.cards.collision.title": "跨层碰撞检测",
        "challenges.cards.collision.problem": "Mario在顶层跳跃可能错误地触发底层barrel的计分，导致不公平的分数累积并破坏游戏平衡。",
        "challenges.cards.collision.solution": "实现了最大80像素阈值的垂直距离验证，并通过多重验证层和Z轴考虑增强碰撞检测系统。",
        "challenges.cards.collision.impact": "消除了误判计分，确保所有关卡的公平游戏和游戏平衡完整性。",
        "challenges.cards.physics.title": "Mario移动物理",
        "challenges.cards.physics.problem": "Mario在离开梯子后会立即失去水平动量，造成不自然的移动和糟糕的游戏感受，让玩家感到沮丧。",
        "challenges.cards.physics.solution": "修改速度重置逻辑以保持空中水平速度，允许自然的空中控制同时保持物理准确性和真实移动。",
        "challenges.cards.physics.impact": "显著改善了玩家体验和控制响应性，使游戏感受更加专业和愉快。",
        "challenges.cards.architecture.title": "可扩展架构设计",
        "challenges.cards.architecture.problem": "关卡实现之间的代码重复和分散在整个代码库中的硬编码值，使维护变得困难且容易出错。",
        "challenges.cards.architecture.solution": "在GameplayScreen基类中创建通用工具方法，通过properties文件和template method pattern集中配置管理。",
        "challenges.cards.architecture.impact": "减少了40%的代码重复，显著提高可维护性，使功能开发更快速，调试更容易。",
        "challenges.metrics.bugs": "误判数",
        "challenges.metrics.accuracy": "准确率",
        "challenges.metrics.improvement": "控制改善",
        "challenges.metrics.response": "响应时间",
        "challenges.metrics.reduction": "代码减少",
        "challenges.metrics.patterns": "Design Pattern",
        "architecture.title": "系统架构",
        "architecture.subtitle": "面向对象设计的全面概览",
        "architecture.hierarchy.title": "类层次结构",
        "architecture.principles.title": "应用的设计原则",
        "architecture.principles.inheritance": "正确使用抽象类和具体实现，实现代码复用和逻辑层次结构",
        "architecture.principles.encapsulation": "通过getter/setter控制访问的私有属性，维护数据完整性",
        "architecture.principles.polymorphism": "基于接口的设计，实现灵活的实体交互和运行时行为选择",
        "architecture.principles.composition": "模块化组件实现复杂行为组装和松耦合",
        "architecture.stats.classes": "类",
        "architecture.stats.abstracts": "抽象类",
        "architecture.stats.interfaces": "Interface",
        "architecture.stats.levels": "继承层级",
        "achievements.title": "项目成就",
        "achievements.subtitle": "可衡量的成就和里程碑",
        "achievements.cards.score.title": "功能满分",
        "achievements.cards.score.desc": "在游戏功能实现上获得满分，满足所有要求",
        "achievements.cards.bugs.title": "零关键Bug",
        "achievements.cards.bugs.desc": "交付生产就绪的代码，具有全面的测试和边缘情况处理",
        "achievements.cards.commits.title": "有意义的Git提交",
        "achievements.cards.commits.desc": "保持出色的版本控制实践，提交信息描述清晰",
        "achievements.cards.delivery.title": "准时交付",
        "achievements.cards.delivery.desc": "在保持质量标准的同时，在截止日期内完成所有项目阶段",
        "achievements.cards.performance.title": "性能优化",
        "achievements.cards.performance.desc": "优化游戏循环和渲染，实现流畅的60fps游戏体验",
        "achievements.cards.documentation.title": "完整文档",
        "achievements.cards.documentation.desc": "所有组件的全面Javadoc注释和README文档",
        "lessons.title": "关键收获",
        "lessons.subtitle": "在整个开发过程中获得的宝贵见解",
        "lessons.cards.testing.title": "全面测试至关重要",
        "lessons.cards.testing.desc": "跨层跳跃bug是通过边缘案例测试发现的。始终测试边界条件和意外的用户行为场景，以确保软件的健壮性。",
        "lessons.cards.testing.highlight": '"边缘案例揭示了代码的真正质量"',
        "lessons.cards.clean.title": "整洁代码回报丰厚",
        "lessons.cards.clean.desc": "移除弃用代码和未使用的接口提高了可维护性，防止了未来的混乱。整洁、文档完善的代码是经得起时间考验的专业代码。",
        "lessons.cards.clean.highlight": '"代码被阅读的次数远多于被编写的次数"',
        "lessons.cards.ux.title": "用户体验很重要",
        "lessons.cards.ux.desc": "Mario空中控制的小改进显著增强了整体游戏感受和玩家满意度。永远不要低估精致交互的影响。",
        "lessons.cards.ux.highlight": '"优秀的UX在缺失之前是无形的"',
        "lessons.cards.architecture.title": "架构决策影响一切",
        "lessons.cards.architecture.desc": "精心设计的继承层次和接口使添加新功能和修复bug变得更加容易。好的架构是对未来开发速度的投资。",
        "lessons.cards.architecture.highlight": '"好的架构赋能，坏的架构限制"',
        "footer.description": "从概念到实现的完整历程",
        "footer.duration": "3周开发周期",
        "footer.grade": "成绩：HD",
        "footer.sections.project": "项目",
        "footer.sections.documentation": "文档",
        "footer.links.source": "源代码",
        "footer.links.download": "下载游戏",
        "footer.links.readme": "README",
        "footer.links.javadoc": "Javadoc",
        "footer.copyright": "© 2024 SWEN20003 Project 2B 开发日志",
        "footer.tech": "技术栈"
    }
};

// ==================== Global State ==================== 
let currentLang = 'en';
let isScrolling = false;

// ==================== Smooth Navigation ==================== 
document.addEventListener('DOMContentLoaded', function() {
    initializeApp();
    setupSmoothScrolling();
    setupScrollAnimations();
    setupLanguageSwitching();
    setupThemeToggle();
    setupMobileMenu();
    setupProgressBar();
    createParallaxEffects();
});

function initializeApp() {
    // Initialize language from localStorage or default to English
    const savedLang = localStorage.getItem('preferredLanguage') || 'en';
    switchLanguage(savedLang);
    
    // Initialize theme from localStorage or default
    const savedTheme = localStorage.getItem('preferredTheme') || 'dark';
    document.documentElement.setAttribute('data-theme', savedTheme);
    updateThemeIcon(savedTheme);
    
    // Setup intersection observer for animations
    setupIntersectionObserver();
    
    // Stagger initial animations
    staggerHeroAnimations();
}

function setupSmoothScrolling() {
    const navLinks = document.querySelectorAll('.nav-links a');
    
    navLinks.forEach(link => {
        link.addEventListener('click', function(e) {
            e.preventDefault();
            
            const targetId = this.getAttribute('href');
            const targetSection = document.querySelector(targetId);
            
            if (targetSection) {
                const headerHeight = document.querySelector('.header').offsetHeight;
                const targetPosition = targetSection.offsetTop - headerHeight;
                
                // Smooth scroll with animation
                smoothScrollTo(targetPosition);
                
                // Update active navigation
                updateActiveNav(targetId);
            }
        });
    });
    
    // Update active navigation on scroll
    const throttledScrollHandler = throttle(updateActiveNavOnScroll, 100);
    window.addEventListener('scroll', throttledScrollHandler);
}

function smoothScrollTo(targetPosition) {
    const startPosition = window.pageYOffset;
    const distance = targetPosition - startPosition;
    const duration = Math.abs(distance) > 1000 ? 1000 : Math.abs(distance) * 0.5;
    let start = null;

    function animation(currentTime) {
        if (start === null) start = currentTime;
        const timeElapsed = currentTime - start;
        const progress = Math.min(timeElapsed / duration, 1);
        
        // Easing function for smooth animation
        const easeInOutCubic = progress < 0.5 
            ? 4 * progress * progress * progress 
            : (progress - 1) * (2 * progress - 2) * (2 * progress - 2) + 1;
        
        window.scrollTo(0, startPosition + distance * easeInOutCubic);
        
        if (timeElapsed < duration) {
            requestAnimationFrame(animation);
        }
    }
    
    requestAnimationFrame(animation);
}

function updateActiveNavOnScroll() {
    if (isScrolling) return;
    
    const sections = document.querySelectorAll('section[id]');
    const scrollPosition = window.scrollY + 150;
    
    sections.forEach(section => {
        const sectionTop = section.offsetTop;
        const sectionHeight = section.offsetHeight;
        const sectionId = section.getAttribute('id');
        
        if (scrollPosition >= sectionTop && scrollPosition < sectionTop + sectionHeight) {
            updateActiveNav(`#${sectionId}`);
        }
    });
}

function updateActiveNav(activeId) {
    const navLinks = document.querySelectorAll('.nav-links a');
    navLinks.forEach(link => {
        link.classList.remove('active');
        if (link.getAttribute('href') === activeId) {
            link.classList.add('active');
        }
    });
}

// ==================== Language Switching ==================== 
function setupLanguageSwitching() {
    const langButtons = document.querySelectorAll('.lang-btn');
    
    langButtons.forEach(btn => {
        btn.addEventListener('click', function() {
            const lang = this.getAttribute('data-lang');
            switchLanguage(lang);
        });
    });
}

function switchLanguage(lang) {
    if (!translations[lang]) return;
    
    currentLang = lang;
    localStorage.setItem('preferredLanguage', lang);
    document.documentElement.setAttribute('data-lang', lang);
    
    // Update all elements with data-i18n attributes
    const elements = document.querySelectorAll('[data-i18n]');
    elements.forEach(element => {
        const key = element.getAttribute('data-i18n');
        const translation = translations[lang][key];
        if (translation) {
            element.textContent = translation;
        }
    });
    
    // Update page title
    document.title = translations[lang]['page.title'];
    
    // Update active language button
    document.querySelectorAll('.lang-btn').forEach(btn => {
        btn.classList.remove('active');
        if (btn.getAttribute('data-lang') === lang) {
            btn.classList.add('active');
        }
    });
    
    // Trigger animation for language change
    triggerLanguageChangeAnimation();
}

function triggerLanguageChangeAnimation() {
    const animatedElements = document.querySelectorAll('[data-i18n]');
    animatedElements.forEach((element, index) => {
        element.style.opacity = '0.7';
        element.style.transform = 'translateY(2px)';
        
        setTimeout(() => {
            element.style.opacity = '1';
            element.style.transform = 'translateY(0)';
        }, index * 10 + 100);
    });
}

// ==================== Theme Toggle ==================== 
function setupThemeToggle() {
    const themeToggle = document.getElementById('themeToggle');
    
    themeToggle.addEventListener('click', function() {
        const currentTheme = document.documentElement.getAttribute('data-theme') || 'dark';
        const newTheme = currentTheme === 'dark' ? 'light' : 'dark';
        
        document.documentElement.setAttribute('data-theme', newTheme);
        localStorage.setItem('preferredTheme', newTheme);
        updateThemeIcon(newTheme);
        
        // Add theme transition animation
        document.body.style.transition = 'background-color 0.3s ease, color 0.3s ease';
        setTimeout(() => {
            document.body.style.transition = '';
        }, 300);
    });
}

function updateThemeIcon(theme) {
    const themeIcon = document.querySelector('#themeToggle i');
    themeIcon.className = theme === 'dark' ? 'fas fa-sun' : 'fas fa-moon';
}

// ==================== Scroll Animations ==================== 
function setupScrollAnimations() {
    const observerOptions = {
        threshold: 0.1,
        rootMargin: '0px 0px -50px 0px'
    };

    const observer = new IntersectionObserver(function(entries) {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                animateElement(entry.target);
            }
        });
    }, observerOptions);

    // Observe timeline items separately for staggered animation
    const timelineItems = document.querySelectorAll('.timeline-item');
    timelineItems.forEach((item, index) => {
        item.style.animationDelay = `${index * 0.2}s`;
        observer.observe(item);
    });

    // Observe other animated elements
    const animatedElements = document.querySelectorAll(`
        .overview-card,
        .challenge-card,
        .lesson-card,
        .achievement-card,
        .tech-category,
        .principle,
        .stat-item
    `);
    
    animatedElements.forEach((element, index) => {
        element.style.opacity = '0';
        element.style.transform = 'translateY(30px)';
        element.style.transition = 'opacity 0.6s ease, transform 0.6s ease';
        element.style.animationDelay = `${(index % 4) * 0.1}s`;
        observer.observe(element);
    });
}

function animateElement(element) {
    element.style.opacity = '1';
    element.style.transform = 'translateY(0)';
    
    if (element.classList.contains('timeline-item')) {
        element.classList.add('animate');
    }
    
    // Special animation for achievement cards
    if (element.classList.contains('achievement-card')) {
        const icon = element.querySelector('.achievement-icon');
        if (icon) {
            setTimeout(() => {
                icon.style.animation = 'pulse 0.6s ease';
            }, 300);
        }
    }
}

function setupIntersectionObserver() {
    const progressBars = document.querySelectorAll('.progress-fill');
    const progressObserver = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                const progressBar = entry.target;
                const width = progressBar.style.width;
                progressBar.style.width = '0%';
                setTimeout(() => {
                    progressBar.style.width = width;
                }, 200);
            }
        });
    }, { threshold: 0.3 });
    
    progressBars.forEach(bar => progressObserver.observe(bar));
}

// ==================== Code Syntax Highlighting ==================== 
function setupCodeHighlighting() {
    const codeBlocks = document.querySelectorAll('pre code');
    
    codeBlocks.forEach(block => {
        let html = block.innerHTML;
        
        // Highlight Java keywords
        html = html.replace(/\b(public|private|protected|class|extends|abstract|implements|if|else|return|boolean|Math|abs|final|static|void|int|double|String)\b/g, 
            '<span style="color: #8b5cf6; font-weight: 600;">$1</span>');
        
        // Highlight strings
        html = html.replace(/"([^"]*)"/g, '<span style="color: #10b981;">\"$1\"</span>');
        
        // Highlight comments
        html = html.replace(/(\/\/.*$)/gm, '<span style="color: #64748b; font-style: italic;">$1</span>');
        
        // Highlight numbers
        html = html.replace(/\b(\d+\.?\d*)\b/g, '<span style="color: #f59e0b;">$1</span>');
        
        // Highlight method calls
        html = html.replace(/(\w+)(\()/g, '<span style="color: #06b6d4;">$1</span>$2');
        
        block.innerHTML = html;
    });
}

// ==================== Stats Counter Animation ==================== 
function animateCounters() {
    const counters = document.querySelectorAll('.stat-number');
    
    counters.forEach(counter => {
        const target = parseInt(counter.textContent.replace(/\D/g, ''));
        const suffix = counter.textContent.replace(/\d/g, '');
        let current = 0;
        const increment = target / 50;
        const duration = 1500;
        const stepTime = duration / 50;
        
        const timer = setInterval(() => {
            current += increment;
            if (current >= target) {
                counter.textContent = target + suffix;
                clearInterval(timer);
            } else {
                counter.textContent = Math.floor(current) + suffix;
            }
        }, stepTime);
    });
}

// Trigger counter animation when stats come into view
function setupStatsObserver() {
    const statsObserver = new IntersectionObserver(function(entries) {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                animateCounters();
                statsObserver.unobserve(entry.target);
            }
        });
    }, { threshold: 0.5 });

    const statsSection = document.querySelector('.hero-stats');
    if (statsSection) {
        statsObserver.observe(statsSection);
    }
}

// ==================== Progress Bar ==================== 
function setupProgressBar() {
    const progressBar = document.createElement('div');
    progressBar.className = 'scroll-progress';
    progressBar.style.cssText = `
        position: fixed;
        top: 0;
        left: 0;
        height: 3px;
        background: linear-gradient(90deg, #6366f1, #8b5cf6);
        z-index: 9999;
        transition: width 0.1s ease;
        width: 0%;
    `;
    
    document.body.appendChild(progressBar);
    
    const throttledProgressUpdate = throttle(() => {
        const scrollPercent = (window.scrollY / (document.documentElement.scrollHeight - window.innerHeight)) * 100;
        progressBar.style.width = Math.min(scrollPercent, 100) + '%';
    }, 16);
    
    window.addEventListener('scroll', throttledProgressUpdate);
}

// ==================== Mobile Menu ==================== 
function setupMobileMenu() {
    const nav = document.querySelector('.nav');
    const navLinks = document.querySelector('.nav-links');
    
    // Create mobile menu button
    const mobileMenuBtn = document.createElement('button');
    mobileMenuBtn.className = 'mobile-menu-btn';
    mobileMenuBtn.innerHTML = '<i class="fas fa-bars"></i>';
    mobileMenuBtn.style.cssText = `
        display: none;
        background: none;
        border: none;
        color: var(--text-secondary);
        font-size: 1.5rem;
        cursor: pointer;
        padding: 0.5rem;
        border-radius: 8px;
        transition: all 0.3s ease;
    `;
    
    nav.appendChild(mobileMenuBtn);
    
    // Toggle mobile menu
    mobileMenuBtn.addEventListener('click', function() {
        navLinks.classList.toggle('mobile-active');
        const icon = this.querySelector('i');
        icon.className = navLinks.classList.contains('mobile-active') 
            ? 'fas fa-times' 
            : 'fas fa-bars';
    });
    
    // Close mobile menu when clicking links
    navLinks.addEventListener('click', function(e) {
        if (e.target.tagName === 'A') {
            navLinks.classList.remove('mobile-active');
            mobileMenuBtn.querySelector('i').className = 'fas fa-bars';
        }
    });
    
    // Handle screen size changes
    function handleResize() {
        if (window.innerWidth <= 768) {
            mobileMenuBtn.style.display = 'block';
        } else {
            mobileMenuBtn.style.display = 'none';
            navLinks.classList.remove('mobile-active');
            mobileMenuBtn.querySelector('i').className = 'fas fa-bars';
        }
    }
    
    window.addEventListener('resize', debounce(handleResize, 250));
    handleResize();
}

// ==================== Parallax Effects ==================== 
function createParallaxEffects() {
    const floatingIcons = document.querySelectorAll('.floating-icon');
    
    window.addEventListener('scroll', throttle(() => {
        const scrolled = window.pageYOffset;
        const rate = scrolled * -0.5;
        
        floatingIcons.forEach((icon, index) => {
            const speed = 0.2 + (index * 0.1);
            icon.style.transform = `translate3d(0, ${rate * speed}px, 0)`;
        });
    }, 16));
}

// ==================== Hero Animations ==================== 
function staggerHeroAnimations() {
    const heroElements = [
        '.hero-badge',
        '.hero-title', 
        '.hero-description',
        '.hero-stats',
        '.hero-actions',
        '.code-window'
    ];
    
    heroElements.forEach((selector, index) => {
        const element = document.querySelector(selector);
        if (element) {
            element.style.opacity = '0';
            element.style.transform = 'translateY(30px)';
            
            setTimeout(() => {
                element.style.transition = 'opacity 0.8s ease, transform 0.8s ease';
                element.style.opacity = '1';
                element.style.transform = 'translateY(0)';
            }, index * 200 + 500);
        }
    });
}

// ==================== Utility Functions ==================== 
function scrollToSection(sectionId) {
    const targetSection = document.getElementById(sectionId);
    if (targetSection) {
        const headerHeight = document.querySelector('.header').offsetHeight;
        const targetPosition = targetSection.offsetTop - headerHeight;
        smoothScrollTo(targetPosition);
        updateActiveNav(`#${sectionId}`);
    }
}

function throttle(func, limit) {
    let inThrottle;
    return function() {
        const args = arguments;
        const context = this;
        if (!inThrottle) {
            func.apply(context, args);
            inThrottle = true;
            setTimeout(() => inThrottle = false, limit);
        }
    }
}

function debounce(func, wait) {
    let timeout;
    return function executedFunction(...args) {
        const later = () => {
            clearTimeout(timeout);
            func(...args);
        };
        clearTimeout(timeout);
        timeout = setTimeout(later, wait);
    };
}

// ==================== Header Background on Scroll ==================== 
window.addEventListener('scroll', throttle(function() {
    const header = document.querySelector('.header');
    const scrolled = window.scrollY > 50;
    
    if (scrolled) {
        header.style.background = document.documentElement.getAttribute('data-theme') === 'light' 
            ? 'rgba(255, 255, 255, 0.98)' 
            : 'rgba(15, 23, 42, 0.98)';
        header.style.backdropFilter = 'blur(20px)';
    } else {
        header.style.background = document.documentElement.getAttribute('data-theme') === 'light' 
            ? 'rgba(255, 255, 255, 0.95)' 
            : 'rgba(15, 23, 42, 0.95)';
        header.style.backdropFilter = 'blur(10px)';
    }
}, 16));

// ==================== Enhanced Card Interactions ==================== 
document.addEventListener('DOMContentLoaded', function() {
    const cards = document.querySelectorAll(`
        .overview-card,
        .challenge-card,
        .lesson-card,
        .achievement-card
    `);
    
    cards.forEach(card => {
        card.addEventListener('mouseenter', function() {
            this.style.transform = 'translateY(-10px) scale(1.02)';
            this.style.zIndex = '10';
        });
        
        card.addEventListener('mouseleave', function() {
            this.style.transform = 'translateY(0) scale(1)';
            this.style.zIndex = '1';
        });
    });
    
    // Initialize all features
    setupCodeHighlighting();
    setupStatsObserver();
});

// ==================== Keyboard Navigation ==================== 
document.addEventListener('keydown', function(e) {
    // ESC key to close mobile menu
    if (e.key === 'Escape') {
        const navLinks = document.querySelector('.nav-links');
        const mobileMenuBtn = document.querySelector('.mobile-menu-btn');
        if (navLinks && navLinks.classList.contains('mobile-active')) {
            navLinks.classList.remove('mobile-active');
            if (mobileMenuBtn) {
                mobileMenuBtn.querySelector('i').className = 'fas fa-bars';
            }
        }
    }
    
    // Arrow keys for navigation between sections
    if (e.altKey) {
        const sections = ['overview', 'journey', 'challenges', 'architecture', 'tech-stack', 'achievements', 'lessons'];
        const currentSection = document.querySelector('.nav-links a.active')?.getAttribute('href')?.substring(1);
        const currentIndex = sections.indexOf(currentSection);
        
        if (e.key === 'ArrowDown' && currentIndex < sections.length - 1) {
            e.preventDefault();
            scrollToSection(sections[currentIndex + 1]);
        } else if (e.key === 'ArrowUp' && currentIndex > 0) {
            e.preventDefault();
            scrollToSection(sections[currentIndex - 1]);
        }
    }
});

// ==================== Performance Monitoring ==================== 
if ('requestIdleCallback' in window) {
    requestIdleCallback(() => {
        // Lazy load non-critical animations
        const lazyAnimations = document.querySelectorAll('.lesson-highlight');
        lazyAnimations.forEach(element => {
            element.style.transition = 'all 0.3s ease';
        });
    });
}

// ==================== Loading States ==================== 
window.addEventListener('load', function() {
    document.body.classList.add('loaded');
    
    // Remove loading states and enable interactions
    setTimeout(() => {
        const allElements = document.querySelectorAll('*');
        allElements.forEach(el => {
            if (el.style.opacity === '0' && !el.hasAttribute('data-keep-hidden')) {
                el.style.opacity = '1';
            }
        });
    }, 1000);
});

// ==================== Error Handling ==================== 
window.addEventListener('error', function(e) {
    console.warn('Non-critical error caught:', e.message);
    // Graceful degradation - ensure basic functionality still works
});

// ==================== Accessibility Enhancements ==================== 
function enhanceAccessibility() {
    // Add skip link
    const skipLink = document.createElement('a');
    skipLink.href = '#overview';
    skipLink.textContent = 'Skip to main content';
    skipLink.className = 'skip-link';
    skipLink.style.cssText = `
        position: absolute;
        top: -40px;
        left: 6px;
        background: var(--primary-color);
        color: white;
        padding: 8px;
        text-decoration: none;
        border-radius: 4px;
        z-index: 10000;
        transition: top 0.3s;
    `;
    
    skipLink.addEventListener('focus', () => {
        skipLink.style.top = '6px';
    });
    
    skipLink.addEventListener('blur', () => {
        skipLink.style.top = '-40px';
    });
    
    document.body.insertBefore(skipLink, document.body.firstChild);
    
    // Enhance focus management
    const focusableElements = document.querySelectorAll('a, button, [tabindex]:not([tabindex="-1"])');
    focusableElements.forEach(el => {
        el.addEventListener('focus', function() {
            this.style.outline = '2px solid var(--primary-color)';
            this.style.outlineOffset = '2px';
        });
        
        el.addEventListener('blur', function() {
            this.style.outline = '';
            this.style.outlineOffset = '';
        });
    });
}

// Initialize accessibility enhancements
document.addEventListener('DOMContentLoaded', enhanceAccessibility); 