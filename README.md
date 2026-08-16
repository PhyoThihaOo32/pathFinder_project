# PathFinder — UI Test Automation Framework

[![CI](https://github.com/PhyoThihaOo32/pathFinder_project/actions/workflows/ci.yml/badge.svg)](https://github.com/PhyoThihaOo32/pathFinder_project/actions/workflows/ci.yml)

A behaviour-driven UI automation framework built with **Selenium 4**, **Cucumber 7** and
**TestNG**. It exercises a real e-commerce flow — sign in, browse, sort, add to cart, check out —
plus a set of trickier browser interactions, and it runs headless in CI on every push.

The suite targets two applications that exist to be automated:

| Application | Used for |
|---|---|
| [saucedemo.com](https://www.saucedemo.com) | The end-to-end shopping journey |
| [the-internet.herokuapp.com](https://the-internet.herokuapp.com) | Checkboxes, native selects, hovers, multi-window |

Both are public practice targets with stable markup, so failures mean a real defect rather than
someone else's site redesign.

---

## Quick start

**Prerequisites:** JDK 17+ and Maven 3.8+. No driver downloads — Selenium Manager resolves the
right driver binary automatically.

```bash
git clone https://github.com/PhyoThihaOo32/pathFinder_project.git
cd pathFinder_project
mvn test
```

### Running the suites

The smoke suite is the default. The regression suite runs every scenario, in parallel.

```bash
mvn test
```

```bash
mvn test -DsuiteXmlFile=testng-regression.xml
```

```bash
mvn test -Dheadless=true -Dbrowser=firefox
```

### Overridable settings

Every key in `src/test/resources/config.properties` can be overridden on the command line, which
is how CI runs the same suite in a different environment without editing a tracked file.

| Property | Default | Notes |
|---|---|---|
| `browser` | `chrome` | `chrome`, `firefox` or `edge` |
| `headless` | `false` | CI sets this to `true` |
| `baseUrl` | `https://www.saucedemo.com` | Application under test |
| `practiceUrl` | `https://the-internet.herokuapp.com` | Interaction practice pages |
| `explicitWaitSeconds` | `15` | Ceiling for every explicit wait |
| `pageLoadTimeoutSeconds` | `30` | Page load budget |

---

## Project structure

```
src/test/java/io/github/phyothihaoo/pathfinder/
├── config/       Configuration      — properties + system-property overrides
├── drivers/      DriverFactory      — browser construction and options
│                 DriverManager      — one WebDriver per thread
├── pages/        BasePage           — explicit-wait helpers shared by every page
│                 LoginPage, InventoryPage, CartPage, Checkout*Page
│                 CheckboxesPage, DropdownPage, HoversPage, WindowsPage
├── hooks/        Hooks              — per-scenario browser lifecycle, failure screenshots
├── steps/        Step definitions, one class per capability
└── runners/      SmokeTestRunner, RegressionTestRunner

src/test/resources/
├── config.properties
└── features/     login, inventory, cart, checkout, interactions
```

## Suites and tags

Scenarios are tagged, and each runner selects by tag — so a scenario joins a suite by being
tagged, never by being listed somewhere.

| Suite | Tag | Scenarios | Parallel | Purpose |
|---|---|---|---|---|
| `testng.xml` | `@smoke` | 4 | no | Critical path; gate every push |
| `testng-regression.xml` | `@regression` | 19 | 3 threads | Full coverage |

## Reports

Cucumber writes HTML and JSON to `target/cucumber-reports/` after each run. **Screenshots are
captured automatically on failure** and embedded in the report, so a red build carries a picture
of the page at the moment it broke.

---

## Design decisions

These are the choices worth explaining, and the reasoning behind each.

**One driver per thread, released in an `@After` hook.**
`DriverManager` keeps the `WebDriver` in a `ThreadLocal`, and `Hooks` quits it in a `finally`
block. Teardown must not live at the end of a `@Then` step: a step-level quit is skipped exactly
when an earlier assertion fails, which is when a browser is most likely to be left running. The
`ThreadLocal` entry is also removed on quit, because TestNG reuses threads and a stale reference
would hand the next scenario a dead browser.

**Explicit waits only — no implicit wait anywhere.**
Implicit and explicit waits do not compose; mixing them produces timeouts that are hard to
predict and harder to debug. Every lookup goes through `BasePage`, which wraps a
`WebDriverWait`.

**No `PageFactory` / `@FindBy`.**
Proxied fields resolve lazily and interact badly with explicit waits, which is a common source
of `StaleElementReferenceException`. Plain `By` constants resolved through a wait are more
predictable.

**No WebDriverManager.**
Selenium Manager has shipped with Selenium since 4.6 and resolves drivers on its own, so the
dependency is redundant.

**Interactions confirm their own outcome.**
This is the least obvious part of the framework and the part that took the most debugging.

The application is a React SPA, and its components re-render as state settles. A click
dispatched into that window lands on a node being replaced, so the handler never runs — while
WebDriver reports success, because it did click something. The page just sits there, and the
failure surfaces later in an unrelated step as a confusing timeout.

Two helpers in `BasePage` close this gap:

- `type` does not return until the field actually holds what was typed, so a fast follow-up
  click cannot submit a half-filled form.
- `clickExpecting` takes the outcome a click should produce — a URL change, a validation
  error, a button swapping to "Remove" — and clicks once more if it does not arrive. Every
  navigation and state-changing click goes through it.

Both exist because of observed intermittent failures, not as speculative defence. The first
version fixed only the checkout submit, where the flake was first seen; CI on a slower runner
promptly failed on the same defect one click earlier, which is what prompted generalising it.

---

## Continuous integration

`.github/workflows/ci.yml` runs the smoke suite headless on every push and pull request to
`master`, and publishes the Cucumber report as a build artifact. The regression suite can be
triggered manually from the Actions tab.
