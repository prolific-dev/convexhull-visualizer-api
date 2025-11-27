# ConvexHull Visualizer API
![Build](https://github.com/prolific-dev/convexhull-visualizer-api/actions/workflows/build.yml/badge.svg)

The ConvexHull Visualizer API is a powerful backend service designed to calculate and visualize convex hulls for a given set of points. It provides efficient algorithms and a RESTful interface for developers to integrate convex hull calculations into their applications.

## Features

- **Efficient Algorithms**: Supports Graham Scan and QuickHull algorithms for 2D and 3D convex hull calculations.
- **RESTful API**: Easy-to-use endpoints for submitting point data and retrieving convex hull results.
- **Visualization Support**: Generates visual representations of convex hulls.
- **Customizable**: Easily extendable for additional features and algorithms.

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.8+

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/prolific-dev/convexhull-visualizer-api.git
   ```
2. Navigate to the project directory:
   ```bash
   cd convexhull-visualizer-api
   ```
3. Build the project:
   ```bash
   ./mvnw clean install
   ```

### Running the Application

To start the application, run:
```bash
./mvnw spring-boot:run
```

The API will be available at `http://localhost:8080`.

## Usage

### Example Input

Submit a JSON payload to one of the following endpoints depending on dimensionality:

- `POST /api/v1/convexhull/2d/compute/hull` — compute the 2D hull (basic response)
- `POST /api/v1/convexhull/2d/compute/full` — compute the 2D hull with full details
- `POST /api/v1/convexhull/3d/compute/hull` — compute the 3D hull (basic response)
- `POST /api/v1/convexhull/3d/compute/full` — compute the 3D hull with full details

Example 2D payload:
```json
{
  "input": [
    "1.0,2.0",
    "3.0,4.0",
    "5.0,6.0"
  ]
}
```

Example 3D payload:
```json
{
  "input": [
    "1.0,2.0,3.0",
    "4.0,5.0,6.0",
    "7.0,8.0,9.0"
  ]
}
```

### Example Output

#### Basic Convex Hull Response
For 2D points, the API will return the following response:
```json
{
  "hull": [
    "1.0,2.0",
    "5.0,6.0",
    "3.0,4.0"
  ]
}
```

For 3D points:
```json
{
  "hull": [
    "1.0,2.0,3.0",
    "7.0,8.0,9.0",
    "4.0,5.0,6.0"
  ]
}
```

#### Detailed Convex Hull Response
For 2D points, the API will return the following detailed response:
```json
{
  "input": [
    "1.0,2.0",
    "3.0,4.0",
    "5.0,6.0"
  ],
  "base": [
    "1.0,2.0",
    "5.0,6.0"
  ],
  "colinear": [
    "3.0,4.0"
  ],
  "hull": [
    "1.0,2.0",
    "5.0,6.0",
    "3.0,4.0"
  ],
  "algorithm": "Graham Scan",
  "computationTimeMs": 12,
  "timestamp": "2025-11-12T12:00:00Z"
}
```

For 3D points:
```json
{
  "input": [
    "1.0,2.0,3.0",
    "4.0,5.0,6.0",
    "7.0,8.0,9.0"
  ],
  "base": [
    "1.0,2.0,3.0",
    "7.0,8.0,9.0"
  ],
  "colinear": [
    "4.0,5.0,6.0"
  ],
  "hull": [
    "1.0,2.0,3.0",
    "7.0,8.0,9.0",
    "4.0,5.0,6.0"
  ],
  "algorithm": "QuickHull",
  "computationTimeMs": 20,
  "timestamp": "2025-11-12T12:00:00Z"
}
```

## Contributing

Contributions are welcome! Please fork the repository and submit a pull request.

## License

This project is licensed under the MIT License. See the LICENSE file for details.

## Contact

For questions or support, please contact [support@prolificdev.com](mailto:support@prolificdev.com).