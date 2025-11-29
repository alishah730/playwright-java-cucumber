# Contributing to Playwright Java Cucumber Framework

Thank you for your interest in contributing to this project! This document provides guidelines and information for contributors.

## 🎯 How to Contribute

### 1. Fork the Repository
- Fork the repository on GitHub
- Clone your fork locally: `git clone https://github.com/your-username/playwright-java-cucumber.git`
- Add the original repository as upstream: `git remote add upstream https://github.com/alishah730/playwright-java-cucumber.git`

### 2. Set Up Your Development Environment
- Ensure you have Java 17+ installed
- Install Maven 3.6+
- Install Playwright browsers: `mvn exec:java -e -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="install"`

### 3. Create a Branch
```bash
git checkout -b feature/your-feature-name
# or
git checkout -b fix/issue-description
```

### 4. Make Your Changes
- Follow the existing code style and conventions
- Write clear, concise commit messages
- Add tests for new functionality
- Update documentation as needed

### 5. Test Your Changes
```bash
# Run all tests
mvn clean test

# Run specific test suites
mvn test -Dgroups="SmokeTest"
```

### 6. Submit a Pull Request
- Push your changes to your fork
- Create a pull request against the main branch
- Provide a clear description of your changes
- Link any relevant issues

## 📋 Guidelines

### Code Style
- Follow Java naming conventions (camelCase for variables, PascalCase for classes)
- Use meaningful variable and method names
- Add JavaDoc comments for public methods and classes
- Keep methods focused and single-purpose
- Use Page Object Model pattern consistently

### Testing Standards
- Write tests for all new features
- Ensure all existing tests pass
- Use descriptive test scenario names
- Follow Given-When-Then structure in Cucumber scenarios
- Add appropriate test tags (@SmokeTest, @End2End, etc.)

### Documentation
- Update README.md for significant changes
- Add JavaDoc comments for new classes/methods
- Include examples in documentation
- Update feature files with clear descriptions

### Commit Messages
Use conventional commit format:
```
type(scope): description

feat(auth): add OAuth2 login support
fix(reporting): resolve HTML report generation issue
docs(readme): update installation instructions
test(login): add edge case scenarios
```

## 🐛 Reporting Issues

### Bug Reports
Please include:
- Clear description of the issue
- Steps to reproduce
- Expected vs actual behavior
- Environment details (Java version, OS, browser)
- Stack traces or error messages
- Screenshots if applicable

### Feature Requests
Please include:
- Clear description of the proposed feature
- Use case and business value
- Proposed implementation approach (if applicable)
- Examples or mockups

## 💡 Development Tips

### Running Tests Locally
```bash
# Run tests in different browsers
mvn test -Dbrowser=Firefox
mvn test -Dbrowser=Webkit

# Run with visible browser (for debugging)
# Modify BasePage.java to set headless=false

# Generate test reports
mvn test
open target/cucumber-html-reports/index.html
```

### Debugging
- Use Playwright's `page.pause()` for interactive debugging
- Enable Playwright traces: `context.tracing().start()`
- Use IDE debugger with breakpoints in step definitions
- Check browser developer tools for element inspection

### Code Generation
Use Playwright codegen for new page interactions:
```bash
mvn exec:java -e -Dexec.mainClass=com.microsoft.playwright.CLI -Dexec.args="codegen https://example.com"
```

## 🔍 Review Process

### Pull Request Checklist
- [ ] Code follows project conventions
- [ ] All tests pass locally
- [ ] New tests added for new functionality
- [ ] Documentation updated
- [ ] Commit messages are clear
- [ ] No merge conflicts

### Review Criteria
- Code quality and maintainability
- Test coverage and quality
- Performance considerations
- Security implications
- Documentation completeness
- Backward compatibility

## 📚 Resources

### Learning Materials
- [Playwright Java Documentation](https://playwright.dev/java/)
- [Cucumber Documentation](https://cucumber.io/docs/)
- [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)
- [Maven Documentation](https://maven.apache.org/guides/)

### Project Structure
- `src/test/java/pages/` - Page Object Model classes
- `src/test/java/stepdefinitions/` - Cucumber step definitions
- `src/test/resources/features/` - Cucumber feature files
- `target/cucumber-*-reports/` - Generated test reports

## 🤝 Community

### Getting Help
- Check existing issues and discussions
- Ask questions in GitHub Discussions
- Join the Playwright community on Discord
- Refer to the official documentation

### Code of Conduct
- Be respectful and inclusive
- Help others learn and grow
- Provide constructive feedback
- Follow project guidelines

## 🎉 Recognition

Contributors will be recognized in:
- README.md contributors section
- Release notes for significant contributions
- GitHub contributor graphs

Thank you for contributing to make this project better! 🚀