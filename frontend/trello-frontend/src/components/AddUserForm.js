import React from 'react';
import { useDispatch } from 'react-redux';
import { addUserToWorkspace } from '../store/workspaceSlice';
import { Formik, Form, Field } from 'formik';
import { TextField, Button, Box } from '@mui/material';

export default function AddUserForm() {
  const dispatch = useDispatch();

  const handleSubmit = (values, { resetForm }) => {
    // Call the Redux action to add the user
    dispatch(addUserToWorkspace(values.email));
    resetForm();
  };

  return (
    <Formik
      initialValues={{ email: '' }}
      onSubmit={handleSubmit}
    >
      <Form>
        <Box display="flex" alignItems="center" gap={2}>
          <Field
            as={TextField}
            type="email"
            name="email"
            label="Add user to workspace"
            variant="outlined"
            fullWidth
          />
          <Button type="submit" variant="contained" color="primary">
            Add User
          </Button>
        </Box>
      </Form>
    </Formik>
  );
}
